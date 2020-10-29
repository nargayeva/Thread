import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Downloader {

    public static void main(String[] args) throws InterruptedException {
        int thread_more = Integer.parseInt(args[0]);

        long start = System.nanoTime();

        String link1 = "http://www.ubicomp.org/ubicomp2003/adjunct_proceedings/proceedings.pdf";
        String link2 = "https://www.hq.nasa.gov/alsj/a17/A17_FlightPlan.pdf";
        String link3 = "https://ars.els-cdn.com/content/image/1-s2.0-S0140673617321293-mmc1.pdf";
        String link4 = "http://www.visitgreece.gr/deployedFiles/StaticFiles/maps/Peloponnese_map.pdf";

        String[] links = {link1,link2,link3,link4};

        File directory1 = new File(System.getProperty("user.home") + "/Desktop/proceedings.pdf");
        File directory2 = new File(System.getProperty("user.home") + "/Desktop/FlightPlan.pdf");
        File directory3 = new File(System.getProperty("user.home") + "/Desktop/mmc1.pdf");
        File directory4 = new File(System.getProperty("user.home") + "/Desktop/Peloponnese_map.pdf");

        File[] files = {directory1,directory2,directory3,directory4};

        int cores = Runtime.getRuntime().availableProcessors();
        // System.out.println(cores);

        if(thread_more == 0) {
        System.out.println("Mode: Single Threaded");
        new withoutThread(link1,directory1).downloader();
        new withoutThread(link2,directory2).downloader();
        new withoutThread(link3,directory3).downloader();
        new withoutThread(link4,directory4).downloader();
        }

        else if(thread_more == 1) {
            System.out.println("Mode: Multi Threaded");
            Thread[] t = new Thread[4];

            for (int i = 0; i < cores; i++) {
                t[i] = new Thread(new withThread(links[i], files[i]));
                t[i].start();
            }

            for (int i = 0; i < cores; i++) {
                t[i].join();
            }
        }

        else {
            System.out.println("Please, enter either one or zero.");
        }

        long finish = System.nanoTime();
        long timeElapsed = finish - start;
        double elapsedTimeInSecond = (double) timeElapsed / 1_000_000_000;

        String time = String.format("%.2f", elapsedTimeInSecond);

        System.out.println();
        System.out.println("Time: " + time + " seconds");
   }
}


class withThread extends Thread{
    String url_link;
    File directory;

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

            if(url_link == "http://www.ubicomp.org/ubicomp2003/adjunct_proceedings/proceedings.pdf"){
                System.out.print("File1 -> done ");
            } else if (url_link == "https://www.hq.nasa.gov/alsj/a17/A17_FlightPlan.pdf"){
                System.out.print("File2 -> done ");
            } else if (url_link == "https://ars.els-cdn.com/content/image/1-s2.0-S0140673617321293-mmc1.pdf"){
                System.out.print("File3 -> done ");
            } else System.out.print("File4 -> done ");

        } catch (IOException ex){
            System.out.println("Exception occurred.");
        }
    }

}


class withoutThread extends Thread{
    String url_link;
    File directory;

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

            if(url_link == "http://www.ubicomp.org/ubicomp2003/adjunct_proceedings/proceedings.pdf"){
                System.out.print("File1 -> done, ");
            } else if (url_link == "https://www.hq.nasa.gov/alsj/a17/A17_FlightPlan.pdf"){
                System.out.print("File2 -> done, ");
            } else if (url_link == "https://ars.els-cdn.com/content/image/1-s2.0-S0140673617321293-mmc1.pdf"){
                System.out.print("File3 -> done, ");
            } else System.out.print("File4 -> done ");
        }

        catch (Exception ex){
            System.out.println("Exception Occurred.");
        }
    }
}
