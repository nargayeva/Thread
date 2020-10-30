import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.text.DecimalFormat;

// Main class
public class Downloader {

    public static String link1 = "http://www.ubicomp.org/ubicomp2003/adjunct_proceedings/proceedings.pdf";
    public static String link2 = "https://www.hq.nasa.gov/alsj/a17/A17_FlightPlan.pdf";
    public static String link3 = "https://ars.els-cdn.com/content/image/1-s2.0-S0140673617321293-mmc1.pdf";
    public static String link4 = "http://www.visitgreece.gr/deployedFiles/StaticFiles/maps/Peloponnese_map.pdf";

    public static void main(String[] args) throws InterruptedException {
        int thread_more = Integer.parseInt(args[0]); // getting 0 or 1 from user

        long start = System.nanoTime(); // start time

        String[] links = {link1,link2,link3,link4}; // links array

        File directory1 = new File(System.getProperty("user.home") + "/Desktop/file1.pdf");
        File directory2 = new File(System.getProperty("user.home") + "/Desktop/file2.pdf");
        File directory3 = new File(System.getProperty("user.home") + "/Desktop/file3.pdf");
        File directory4 = new File(System.getProperty("user.home") + "/Desktop/file4.pdf");

        File[] files = {directory1,directory2,directory3,directory4}; // link directories array

        int cores = Runtime.getRuntime().availableProcessors(); // number of cores

        // SINGLE THREADED
        if(thread_more == 0) {
        System.out.println("Mode: Single Threaded");
        new withoutThread(link1,directory1).downloader();
        new withoutThread(link2,directory2).downloader();
        new withoutThread(link3,directory3).downloader();
        new withoutThread(link4,directory4).downloader();
        }

        // MULTI THREADED
        else if(thread_more == 1) {
            System.out.println("Mode: Multi Threaded");
            Thread[] t = new Thread[cores]; // Thread array

            for (int i = 0; i < cores; i++) {
                t[i] = new Thread(new withThread(links[i], files[i]));
                t[i].start();
            }

            for (int i = 0; i < cores; i++) {
                t[i].join(); // for each thread to wait for others to terminate
            }
        }

        else {
            System.out.println("Please, enter either one or zero.");
        }

        long finish = System.nanoTime(); // end time
        long timeElapsed = finish - start; // elapsed time
        double elapsedTimeInSecond = (double) timeElapsed / 1_000_000_000; // converting nano second to second

        DecimalFormat df = new DecimalFormat("#.##");
        String time = df.format(elapsedTimeInSecond);

        System.out.println();
        System.out.println("Time: " + time + " seconds");
   }
}

// Class with thread
class withThread extends Thread{
    String url_link;
    File directory;
    Downloader downloader = new Downloader();

    public withThread(String url_link, File directory){
        this.url_link = url_link;
        this.directory = directory;
    }

    @Override
    public void run() {
        try {

            URL url = new URL(url_link);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            BufferedInputStream input = new BufferedInputStream((http.getInputStream()));
            FileOutputStream output = new FileOutputStream(this.directory);
            BufferedOutputStream buffered_output = new BufferedOutputStream(output,1024);
            byte[] buffer = new byte[1024];

            int read;

            while((read = input.read(buffer,0,1024)) >= 0){
                buffered_output.write(buffer,0,read);
            }

            buffered_output.close();
            input.close();

            if(url_link == downloader.link1){
                System.out.print("File1 -> done, ");
            } else if (url_link == downloader.link2){
                System.out.print("File2 -> done, ");
            } else if (url_link == downloader.link3){
                System.out.print("File3 -> done, ");
            } else if (url_link == downloader.link4){
                System.out.print("File4 -> done, ");
            }

        } catch (IOException ex){
            System.out.println("Exception occurred.");
        }
    }
}

// Class without thread
class withoutThread {
    String url_link;
    File directory;
    Downloader downloader = new Downloader();

    public withoutThread(String url_link, File directory){
        this.url_link = url_link;
        this.directory = directory;
    }

    public void downloader() {

        try {
            int read;

            URL url = new URL(url_link);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            BufferedInputStream input = new BufferedInputStream((http.getInputStream()));
            FileOutputStream output = new FileOutputStream(this.directory);
            BufferedOutputStream buffered_output = new BufferedOutputStream(output, 1024);
            byte[] buffer = new byte[1024];

            while ((read = input.read(buffer, 0, 1024)) >= 0) {
                buffered_output.write(buffer, 0, read);
            }

            buffered_output.close();
            input.close();

            if(url_link == downloader.link1){
                System.out.print("File1 -> done, ");
            } else if (url_link == downloader.link2){
                System.out.print("File2 -> done, ");
            } else if (url_link == downloader.link3){
                System.out.print("File3 -> done, ");
            } else if (url_link == downloader.link4){
                System.out.print("File4 -> done");
            }
        }

        catch (Exception ex){
            System.out.println("Exception Occurred.");
        }
    }
}
