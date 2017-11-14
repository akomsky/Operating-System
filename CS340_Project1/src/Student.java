import java.util.Random;

public class Student extends Thread {
	public void msg(String m){
		System.out.println("[" + (System.currentTimeMillis()-Main.time) + "]" + getName() + ": " + m);
	}
	private int IDnum;
	
	// default constructor
	public Student(int id) {
		setName("Student-" + id);
		IDnum = id;
	}
	public int getIdNum() {
		return IDnum;
	}
	public synchronized void increment() {
		Main.student_count.incrementAndGet();
	}
	public synchronized int getNumStud() {
		return Main.student_count.get();
	}
	
	// override the run method
	public void run() {
		goToSchool();
		if(getIdNum() != 0) {
			try {
				Main.student[getIdNum()-1].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		msg("GOES HOME");
	}
	
	public void goToSchool() {
		Random rand = new Random();
		try {
			sleep(rand.nextInt(2000));
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		msg("Student " + getIdNum() + " arrives to school and waiting outside classroom");
		rush_inside();
	}
	
	public synchronized void rush_inside() {
		// busy wait until instructor arrives
		while (Main.Instructor_arrived == false){}; 
		
		// student who missed an exam need to take 2nd test in order
		// to ensure each student takes 2 exams.
		if((Main.examNumber>1) && (Main.num_exams_taken[getIdNum()] == 0))
			setPriority(Thread.MAX_PRIORITY);
		// student enter classroom- increase their priority
		else {
			int i = getPriority();
			msg("Increase priority");
			setPriority(i++);
			try {
				sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
		enterClassroom(); // students enter the classroom
		setPriority(Thread.NORM_PRIORITY);
	}
	public void enterClassroom() {
		// allow students to enter up to capacity 
		if ( (getNumStud() < (Main.capacity)) && (Main.num_exams_taken[getIdNum()] < 2) && (Main.exam_starts==false) ) { 
			increment(); // increase the number of students entered
			msg("Student " + getIdNum() + " enters the classroom for exam "+ Main.examNumber); 
			Main.take_exam[getIdNum()][Main.examNumber-1] = true; // signal that the student has taken this exam
			Main.num_exams_taken[getIdNum()]++;
			Main.examQ.addElement(getIdNum()); // add the student's ID num who entered 
			
			// now the student waits for exam to start
			try {
				sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// students who are inside the classroom work on exam
			try {
				sleep((long) 7000);
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
			
			// they take a break 
			Random rand = new Random();
			try {
				sleep(rand.nextInt(1000));
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
			if (Main.examNumber < 4)
				rush_inside();
			
		} // end if 
		
		else if (Main.take_exam[getIdNum()][Main.examNumber-1] == false && Main.num_exams_taken[getIdNum()] < 2) {
			Main.door_open = false;
			msg("Student " + getIdNum() + " missed exam"); 
			yield();
			yield();
			while (Main.door_open == false) {};
			if (Main.examNumber < 4)
				rush_inside();
		}
		
		else {
			msg("Student " + getIdNum() + " has already taken 2 exams");
		}
		
		// if took 2 exams already, busy wait until grades are posted to go home
		while(Main.grades_posted == false && Main.num_exams_taken[getIdNum()] == 2) {};
	
	} // end enterClassroom
}
