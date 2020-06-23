/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VoteSMA;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author claire
 */
public class CsvHelper {

    String separator;
    File file;

    public CsvHelper(String separator, String f, boolean del, String dir) throws IOException {

        this.separator = separator;
        
        if (del) {
            file = getResource(f,dir);
            deleteFile();
        }
        file = getResource(f,dir);
        file.createNewFile();

    }

    public String getResourcePath(String fileName,String dir) throws IOException {
        final File f = new File("");
        
        File directory=new File(f.getAbsolutePath()+File.separator+ dir);
        if(!directory.exists()){
            directory.mkdir();
        }
                
        final String dossierPath = f.getAbsolutePath()+File.separator+ dir + File.separator + fileName;
        return dossierPath;
    }

    public File getResource(String fileName,String dir) throws IOException {
        final String completeFileName = getResourcePath(fileName,dir);
        File file = new File(completeFileName);
        return file;
    }

    public String[] parseLine(String line) {
        return line.split(separator);
    }

    public List<String> readFile() throws FileNotFoundException, IOException {

        FileReader fr = null;
        List<String> result = new ArrayList<String>();

        fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            result.add(line);
        }
        br.close();
        fr.close();
        return result;

    }

    public void write(List<Map<String, String>> mappedData, boolean writeTitle) throws IOException {

        if (mappedData == null) {
            throw new IllegalArgumentException("List null");
        }

        final Map<String, String> oneData = mappedData.get(0);

        final String[] titles = new String[oneData.size()];

        int i = 0;
        for (String key : oneData.keySet()) {
            titles[i++] = key;
        }
        write(mappedData, titles, writeTitle);
    }

    public boolean deleteFile() {
        return file.delete();
    }

    public void write(List<Map<String, String>> mappedData, String[] titles, boolean writeTitle) throws IOException {
        boolean first = true;
        if (mappedData == null) {
            throw new IllegalArgumentException("List null");
        }

        if (titles == null) {
            throw new IllegalArgumentException("Title null");
        }

        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        if (writeTitle) {
            first = true;
            for (String title : titles) {
                if (first) {
                    first = false;
                } else {
                    bw.write(separator);
                }
                write(title, bw);
            }
            bw.write("\n");
        }

        for (Map<String, String> oneData : mappedData) {
            first = true;
            for (String title : titles) {
                if (first) {
                    first = false;
                } else {
                    bw.write(separator);
                }
                final String value = oneData.get(title);
                write(value, bw);

            }
            bw.write("\n");

            bw.close();
            fw.close();
        }
    }

    private void write(String value, BufferedWriter bw) throws IOException {

        if (value == null) {
            value = "";
        }

        boolean needQuote = false;

        if (value.contains("\n")) {
            needQuote = true;
        }

        if (value.contains(separator)) {
            needQuote = true;
        }

        if (value.contains("\"")) {
            needQuote = true;
            value = value.replaceAll("\"", "\"\"");
        }

        if (needQuote) {
            value = "\"" + value + "\"";
        }

        bw.write(value);
    }
}
