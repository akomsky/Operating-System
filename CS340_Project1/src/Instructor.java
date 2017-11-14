import java.util.Arrays;
import java.util.Random;

public class Instructor extends Thread {
	public void msg(String m){
		System.out.println("[" + (System.currentTimeMillis()-Main.time) + "]" + getName() + ": " + m);
	}
	// default constructor
	public Instructor(int id) {
		setName("Instructor-" + id);
	}
	public synchronized void resetNumStud() {
		Main.student_count.set(0);
	}
	public void run() {
		goToSchool();
	}
	public void goToSchool() {
		Random rand = new Random();
		try {
			sleep(rand.nextInt(2000));
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		msg("Instructor arrives at school");
		enterClassroom();
		postGrades();
		Main.grades_posted = true;
	}
	public void enterClassroom() {
		Main.Instructor_arrived = true;
		// the Instructor wait for exam to begin
		msg("Instructor enters classroom");
		try {
			sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		Main.door_open = false;
		Main.exam_starts = true;
		Main.door_open = false;
		msg("Instructor begins exam " + Main.examNumber);
		// Instructor sleeps throughout the exam
		try {
			sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		resetNumStud();
		// exam is over so Instructor collects exams (by interrupting the threads in correct order)
		msg("Exam " + Main.examNumber + " over");
		for (int i=0; i<Main.examQ.size(); i++){
			if (!isInterrupted())
				Main.student[(int) Main.examQ.get(i)].interrupt();
				msg("Student " + Main.examQ.get(i) + " leaves classroom (interrupted)");
		}
		
		// instructor takes break of random time before next exam
		Main.exam_starts = false;
		Main.examNumber++;
		Main.Instructor_arrived = false; // he leaves on break
		msg("Instructor takes a break");
		Random rand = new Random();
		try {
			sleep(rand.nextInt(1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// now he reenters the classroom and allows students
		// to come in for the next exam
		// initialize everything:
		//Main.examNumber++;
		if (Main.examNumber < 4) {
			for (int i=0; i<Main.Nstudents; i++){
				Main.take_exam[i][Main.examNumber-1] = false;
			}
		}
		Main.examQ.clear();
		Main.door_open = true;
		// instructor reenters classroom
		while (Main.examNumber < 4){
			enterClassroom();
		}
	}
	public void postGrades(){
		Random rand = new Random();
		for(int i=0; i<3; i++) {
			System.out.println("Exam " + (i+1));
			
			for (int j=0; j<Main.Nstudents; j++) {
				if (Main.take_exam[j][i] == true) {
					System.out.print("Student " + j + "\t" + "Grade: " + 
									(rand.nextInt(91) + 10) + "\n");
				}
				else 
					System.out.print("Student " + j + "\t" + "Grade: " + 0 + "\n");
			}
			System.out.println("");
		}
	}
//	public void postGrades1() {
//		Random rand = new Random();
//		for(int i=0; i<Main.Nstudents; i++) {
//			System.out.println("Student " + i);
//			
//			for(int j=0; j<3; j++) {
//				if (Main.take_exam[i][j]==true)
//					System.out.print("Exam " + (j+1) + ":" + (rand.nextInt(91)+10) + "\t");
//				else 
//					System.out.print("Exam " + (j+1) + ":" + 0 + "\t");
//			}
//			System.out.println("\n");
//		}
//	}
}
