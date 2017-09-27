package com.joyent.workloadgenerator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

public class GenerateWorkload {

    int depth;
    int numberOfSubs = 2;
    String filesize;
    int noOfFiles;
    // The default is 3 characters, paths can get long so we want to make it
    // configurable.
    int directoryNameLength = 3;
    String resultFile;
    private List<String> leaves = new LinkedList<String>();

    public static String GIGA_BYTES = "GB";
    public static String KILO_BYTES = "KB";
    
    int readRatio = 20;
    int writeRatio = 80;

    public GenerateWorkload(int depth, int noOfFiles,String filesize, int directoryNameLength, String resultFile,int readRatio) {
        super();
        this.noOfFiles = noOfFiles;
        this.depth = depth - 1;
        this.filesize = filesize;
        this.directoryNameLength = directoryNameLength;
        this.resultFile = resultFile;
        this.readRatio = readRatio;
        this.writeRatio = 100 - readRatio;
    }

    public GenerateWorkload(int depth, String filesize, String resultFile) {
        super();
        this.depth = depth;
        this.filesize = filesize;
        this.resultFile = resultFile;
    }

    /**
     * This will generate a directory structure that has the depth indicated.
     * This will use a helper function, and
     */
    @SuppressWarnings("unchecked")
    public void generateTree() {
        StorageData stor = new StorageData("manta");
        stor.addConfig("manta.verify_uploads=false");
        stor.addConfig("retries=3");
        stor.addConfig("manta.client_encryption=true");
        stor.addConfig("manta.encryption_key_id=cosbench-test");
        stor.addConfig("manta.encryption_algorithm=AES128/CTR/NoPadding");
        stor.addConfig("manta.encryption_key_bytes_base64=MKBUne726xyvMK2P0Y2DXg==");
        WorkflowData wfd = new WorkflowData();
        WorkloadData data = new WorkloadData("Generated Workload", "This will generate a tree", "", stor, wfd);
        WorkstageData prepare, main, dispose;
        prepare = new WorkstageData("prepare");
        main = new WorkstageData("main");
        dispose = new WorkstageData("dispose");
        List<WorkData> directories = createDirectories("", 0);
        Collections.sort(directories);
        List<WorkstageData> inits = new LinkedList<WorkstageData>();
        for(int i =0;i < depth;i++) { 
            inits.add(new WorkstageData("init" + i));
        }
        for(WorkData d:directories) { 
           int level = d.getLevel();
           inits.get(level - 1).addWork(d);
        }
        for(WorkstageData cur:inits) { 
            wfd.addWorkStage(cur);
        }
        
        main = createMain(main);
        for(String leaf:leaves) { 
            prepare.addWork(new WorkData("prepare","1",String.format("%s;objects=r(1,%d);sizes=c(1)KB", leaf,noOfFiles,noOfFiles)));
        }
        
        wfd.addWorkStage(prepare);
        wfd.addWorkStage(main);
        wfd.addWorkStage(dispose);
      
        data.generateDocument("/tmp/test.xml");
        
        // String name, String description, String config, StorageData storage,
        // WorkflowData wfdata
    }
    
    /**
     * Helper function for putting together the main phase.
     * @param main
     * @return
     */
    private WorkstageData createMain(WorkstageData main) { 
        List<OperationData> operations = new LinkedList<OperationData>();
        for(int i = 0;i < this.readRatio;i++) {
            String type = "read";
            String ratio = "1";
            String config = leaves.get(i);
            operations.add(new OperationData(type,ratio,config));
        }
        String writeConfig = "containers=r(1,1);objects=r(1,);sizes=c(1)KB;makeContainer=true";
        operations.add(new OperationData("write",this.writeRatio + "",writeConfig));
        //name="main" runtime="30" config="" type="main" workers="1"
        WorkData work = new WorkData("main", "1", "");
        work.setOperation(operations);    
        main.addWork(work);
        return main;
    }

    /*
     * So this confusing part of this is that we are writing a command create
     * storage with prefix, and number, We are then trying to track those
     * prefixes and numbers on our way to creating this tree.
     * 
     * So if I give is a,2 -> I will have directories a1,a2 When I iterate on
     * that the directories it should be fx(a1,2),fx(a2,2),
     */
    public List<WorkData> createDirectories(String path, int currentDepth) {
        List<WorkData> work = new LinkedList<WorkData>();
        String config;
        String prefix = RandomStringUtils.randomAlphabetic(directoryNameLength - 1);
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

    public static void main(String[] args) {
        GenerateWorkload gw = new GenerateWorkload(7,3,"5KB", 3, "/tmp/treeBuild.xml",20);
        gw.generateTree();
        System.out.println();
    }
}
