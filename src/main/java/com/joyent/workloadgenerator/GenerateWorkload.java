package com.joyent.workloadgenerator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * This will gather the info and create the objects necessary for the document, and will write it out to the file
 * system.
 *
 * @author DouglasAnderson
 *
 */
public class GenerateWorkload {
    /**
     * The depth of the tree that will be created.
     */
    private int depth;
    /**
     * The number of sub-directories that will be created at each level.
     */
    private int numberOfSubs;
    /**
     * The size of the files that are created..
     */
    private String filesize;
    /**
     * The number of files in the directory.
     */
    private int noOfFiles;
    /**
     * The length of directories and subdirectories, defaults to 3.
     */
    private int directoryNameLength = DEFAULT_NAME_LENGTH;
    /**
     * The location of the file that is generated.
     */
    private String resultFile;
    /**
     * This is a list of directories that are at the bottom of the tree.
     */
    private List<String> leaves = new LinkedList<String>();
    /**
     * A short for 1000 MB.
     */
    public static final String GIGA_BYTES = "GB";
    /**
     * A short for 1000 Bytes.
     */
    public static final String KILO_BYTES = "KB";
    /**
     * Properties that will be set through a properties file, used for configuring this object.
     */
    private Properties props;
    /**
     * The ratio of read the main section of a workload file.
     */
    private int readRatio = DEFAULT_READ;
    /**
     * The ratio of write in a single workload file..
     */
    private int writeRatio = TOTAL_RATIO - DEFAULT_READ;

    /**
     * The total raio.
     */
    public static final int TOTAL_RATIO = 100;
    /**
     * Default read.
     */
    public static final int DEFAULT_READ = 20;
    /**
     * Default name length.
     */
    public static final int DEFAULT_NAME_LENGTH = 3;

    /**
     * Default constructor, this will use the properties to set all fields.
     *
     * @param props - A property file.
     */
    public GenerateWorkload(final Properties props) {
        this.props = props;
        this.noOfFiles = Integer.parseInt(props.getProperty("leaves"));
        this.depth = Integer.parseInt(props.getProperty("depth")) - 1;
        System.out.println("Depth is : " + this.depth);
        this.numberOfSubs = Integer.parseInt(props.getProperty("branches"));
        this.filesize = props.getProperty("filesize");
        this.directoryNameLength = Integer.parseInt(props.getProperty("directoryNameLength"));
        this.resultFile = props.getProperty("resultFile");
        this.readRatio = Integer.parseInt(props.getProperty("readRatio"));
        this.writeRatio = TOTAL_RATIO - readRatio;
    }

    /**
     * Simple constructor, used for debugging this mostly since the no arg should be used in general.
     *
     * @param depth - something.
     * @param noOfFiles - something.
     * @param filesize - something.
     * @param directoryNameLength - something.
     * @param resultFile - something.
     * @param readRatio - something.
     */
    public GenerateWorkload(final int depth, final int noOfFiles, final String filesize, final int directoryNameLength,
            final String resultFile, final int readRatio) {
        super();
        this.noOfFiles = noOfFiles;
        this.depth = depth - 1;
        this.directoryNameLength = directoryNameLength;
        this.resultFile = resultFile;
        this.readRatio = readRatio;
        this.writeRatio = TOTAL_RATIO - readRatio;
    }

    /**
     * This will generate a directory structure that has the depth indicated.
     */
    @SuppressWarnings("unchecked")
    public void generateTree() {
        WorkloadData data = new WorkloadData("Generated Workload", "This will generate a tree", "");
        StorageData stor = new StorageData("manta");
        stor.addConfig("manta.verify_uploads=false");
        stor.addConfig("retries=3");
        stor.addConfig("manta.client_encryption=true");
        stor.addConfig("manta.encryption_key_id=cosbench-test");
        stor.addConfig("manta.encryption_algorithm=AES128/CTR/NoPadding");
        stor.addConfig("manta.encryption_key_bytes_base64=MKBUne726xyvMK2P0Y2DXg==");
        data.setStorage(stor);

        WorkflowData wfd = new WorkflowData();
        data.setWfdata(wfd);
        WorkstageData prepare, main, dispose;
        prepare = new WorkstageData("prepare");
        main = new WorkstageData("main");
        dispose = new WorkstageData("dispose");
        List<WorkData> directories = createDirectories("", 0);
        System.out.println("We are adding: " + directories.size() + " directories");
        Collections.sort(directories);
        List<WorkstageData> inits = new LinkedList<WorkstageData>();
        for (int i = 0; i < depth; i++) {
            inits.add(new WorkstageData("init" + i));
        }
        for (WorkData d : directories) {
            int level = d.getLevel();
            inits.get(level - 1).addWork(d);
        }
        for (WorkstageData cur : inits) {
            wfd.addWorkStage(cur);
        }

        main = createMain(main);
        for (String leaf : leaves) {
            prepare.addWork(new WorkData("prepare", "1",
                    String.format("%s;objects=r(1,%d);sizes=c(1)KB", leaf, noOfFiles, noOfFiles)));
        }

        wfd.addWorkStage(prepare);
        wfd.addWorkStage(main);
        wfd.addWorkStage(dispose);

        data.generateDocument(resultFile);
    }

    /**
     * Helper function for putting together the main phase.
     *
     * @param main - This will create the necessary components.
     * @return - returns a work stage data.
     */
    private WorkstageData createMain(final WorkstageData main) {
        List<OperationData> operations = new LinkedList<OperationData>();
        for (int i = 0; i < this.readRatio; i++) {
            String type = "read";
            String ratio = "1";
            String config = leaves.get(i);
            operations.add(new OperationData(type, ratio, config));
        }
        String writeConfig = "containers=r(1,1);objects=r(1,);sizes=c(1)KB;makeContainer=true";
        operations.add(new OperationData("write", this.writeRatio + "", writeConfig));
        WorkData work = new WorkData("main", "1", "");
        work.setOperation(operations);
        main.addWork(work);
        return main;
    }

    /**
     * createDirectories is a recursive method that creates a set of WorkData.
     *
     * So this confusing part of this is that we are writing a command create storage with prefix, and number, We are
     * then trying to track those prefixes and numbers on our way to creating this tree.
     *
     * So if I give is a,2 -> I will have directories a1,a2 When I iterate on that the directories it should be
     * fx(a1,2),fx(a2,2),
     *
     * @param path - current path that we are on.
     * @param currentDepth - the depth that we are at.
     * @return - a list of work data this will be a representation of a tree structure.
     */
    public List<WorkData> createDirectories(final String path, final int currentDepth) {
        List<WorkData> work = new LinkedList<WorkData>();
        String config;
        String prefix = RandomStringUtils.randomAlphabetic(directoryNameLength - 1);
        System.out.println(String.format("Current depth %d total depth : %d", currentDepth, depth));
        if (currentDepth == 0) {
            // Special case, we do not want the directory prepended at the root.
            prefix = RandomStringUtils.randomAlphabetic(directoryNameLength - 1);
            config = String.format("cprefix=%s;containers=r(1,%d)", prefix, numberOfSubs);
        } else {
            final String template = "cprefix=%s%s;containers=r(1,%d)";
            config = String.format(template, path, prefix, numberOfSubs);
            // This will create the name for the new sets of directories.
        }
        work.add(new WorkData("init", "1", config));
        // If we are not at the lowest part, we will create numberOfSubs
        // directories, and add each of the collections to the previous.
        System.out.println(String.format("Current depth %d total depth : %d", currentDepth, (depth - 1)));
        if (currentDepth == depth - 1) {
            leaves.add(config);
            System.out.println(String.format("adding leaf %s", config));
            return work;
        }
        for (int i = 0; i < numberOfSubs; i++) {
            if (currentDepth == 0) {
                work.addAll(createDirectories(String.format("%s%d/", prefix, i + 1), currentDepth + 1));
            } else {
                work.addAll(createDirectories(String.format("%s%s%d/", path, prefix, i + 1), currentDepth + 1));
            }
        }
        return work;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "GenerateWorkload [depth=" + depth + ", numberOfSubs=" + numberOfSubs + ", filesize=" + filesize
                + ", noOfFiles=" + noOfFiles + ", directoryNameLength=" + directoryNameLength + ", resultFile="
                + resultFile + ", leaves=" + leaves + ", props=" + props + ", readRatio=" + readRatio + ", writeRatio="
                + writeRatio + "]";
    }

    /**
     * This is a mina method.
     *
     * @param args - something.
     * @throws IOException - other thing.
     */
    public static void main(final String[] args) throws IOException {
        String propFileName = "geneartor.properties";
        if (System.getProperty("config") != null) {
            propFileName = System.getProperty("config");
        }
        Properties prop = new Properties();
        InputStream input = new FileInputStream(propFileName);
        prop.load(input);
        GenerateWorkload g = new GenerateWorkload(prop);
        System.out.println(g.toString());
        g.generateTree();
    }
}
