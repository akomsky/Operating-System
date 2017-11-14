import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
	public static long time = System.currentTimeMillis();
	public static Student[] student;
	public static Instructor instructor;
	public static volatile boolean Instructor_arrived;
	public static volatile boolean[][] take_exam;
	public static volatile int[] num_exams_taken;
	public static int capacity;
	public static int Nstudents;
	public static volatile boolean exam_starts;
	public static volatile int examNumber;
	public static Vector examQ; 
	public static volatile AtomicInteger student_count;
	public static volatile AtomicInteger temp; 
	public static volatile boolean door_open;
	public static volatile boolean grades_posted;
	
	
	public static void main(String args[]) {
		capacity = Integer.parseInt(args[0]); // 10
		Nstudents = Integer.parseInt(args[1]); // 14
		examNumber = 1;
		student_count = new AtomicInteger(0);
		temp = new AtomicInteger(Main.Nstudents-1);
		door_open = false;
		grades_posted = false;
		take_exam = new boolean[Nstudents][3];
		num_exams_taken = new int[Nstudents];
		Arrays.fill(num_exams_taken, 0);
		student = new Student[Nstudents];
		for (int i=0; i<Nstudents; i++){
			Arrays.fill(take_exam[i], false);
		}
		Instructor_arrived = false;
		exam_starts = false;
		examQ = new Vector(14);
		for (int i=0; i<Nstudents; i++) {
			student[i] = new Student(i);
		}
		instructor = new Instructor(1);
		for (int i=0; i<Nstudents; i++){
			student[i].start();
		}
		instructor.start();
	}
}
