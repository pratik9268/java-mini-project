// package in.ac.adit.pwj.miniproject.jobs;

import java.io.*;
import java.util.*;

class User {
    protected String username;
    protected String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

class Employer extends User {
    public Employer(String username, String password) {
        super(username, password);
    }

    public void postJob(Job job) {
        JobManager.addJob(job);
    }
}

class JobSeeker extends User {
    public JobSeeker(String username, String password) {
        super(username, password);
    }

    public void applyForJob(String jobId) {
        Application application = new Application(this.username, jobId);
        JobManager.addApplication(application);
    }

    public void searchJobs(String keyword) {
        System.out.println("Search Results for '" + keyword + "':");
        JobManager.searchJobs(keyword);
    }
}

class Job {
    private String jobId;
    private String title;
    private String description;

    public Job(String jobId, String title, String description) {
        this.jobId = jobId;
        this.title = title;
        this.description = description;
    }

    public String getJobId() {
        return jobId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Job ID: " + jobId + ", Title: " + title + ", Description: " + description;
    }
}

class Application {
    private String username;
    private String jobId;

    public Application(String username, String jobId) {
        this.username = username;
        this.jobId = jobId;
    }

    @Override
    public String toString() {
        return "User: " + username + " applied for Job ID: " + jobId;
    }
}

class JobManager {
    private static Map<String, Job> jobMap = new HashMap<>();
    private static List<Application> applicationList = new ArrayList<>();

    public static synchronized void addJob(Job job) {
        jobMap.put(job.getJobId(), job);
        saveJobToFile(job);  // Saving job to a .txt file
    }

    public static synchronized void addApplication(Application application) {
        applicationList.add(application);
        saveApplicationToFile(application);  // Saving application to a .txt file
    }

    public static void listJobs() {
        for (Job job : jobMap.values()) {
            System.out.println(job);
        }
    }

    public static void searchJobs(String keyword) {
        boolean found = false;
        for (Job job : jobMap.values()) {
            if (job.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                job.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(job);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No jobs found matching the keyword: " + keyword);
        }
    }

    // File handling for job listings
    private static void saveJobToFile(Job job) {
        try (FileWriter fw = new FileWriter("jobs.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(job);  // Write the job details to jobs.txt
        } catch (IOException e) {
            System.out.println("Error saving job to file: " + e.getMessage());
        }
    }

    // File handling for applications
    private static void saveApplicationToFile(Application application) {
        try (FileWriter fw = new FileWriter("applications.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(application);  // Write the application details to applications.txt
        } catch (IOException e) {
            System.out.println("Error saving application to file: " + e.getMessage());
        }
    }
}

// Main class to run the job portal system
public class JobPortal {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Sample employer login
        System.out.print("Enter employer username: ");
        String employerUsername = scanner.nextLine();
        System.out.print("Enter employer password: ");
        String employerPassword = scanner.nextLine();
        Employer employer = new Employer(employerUsername, employerPassword);

        // Post a job
        System.out.print("Enter Job ID: ");
        String jobId = scanner.nextLine();
        System.out.print("Enter Job Title: ");
        String jobTitle = scanner.nextLine();
        System.out.print("Enter Job Description: ");
        String jobDescription = scanner.nextLine();
        
        employer.postJob(new Job(jobId, jobTitle, jobDescription));

        // Sample job seeker login
        System.out.print("Enter job seeker username: ");
        String jobSeekerUsername = scanner.nextLine();
        System.out.print("Enter job seeker password: ");
        String jobSeekerPassword = scanner.nextLine();
        JobSeeker jobSeeker = new JobSeeker(jobSeekerUsername, jobSeekerPassword);

        // Apply for a job
        System.out.print("Enter Job ID to apply for: ");
        String jobIdToApply = scanner.nextLine();
        jobSeeker.applyForJob(jobIdToApply);

        // Search for jobs
        System.out.print("Enter keyword to search for jobs: ");
        String keyword = scanner.nextLine();
        jobSeeker.searchJobs(keyword);

        System.out.println("Current Job Listings:");
        JobManager.listJobs();

        scanner.close();
    }
}
