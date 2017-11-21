package com.app.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.app.entities.Entity;
import com.app.entities.Exercise;
import com.app.entities.Posture;
import com.app.graphics.avatar.BodyModelImpl.JOINT_TAG;

public class DAO implements IDatabase{
	private static Connection c = null;
	private String connectionString = "jdbc:sqlite:digitalAssistant.db";

	private String createTablePostureStmnt = "CREATE TABLE "+Posture.class.getSimpleName() +
			" (ID INTEGER PRIMARY KEY NOT NULL," +
			"latest_progress INTEGER DEFAULT 1,highest_progress INTEGER DEFAULT 1,"+
			" duration INTEGER DEFAULT 15, NAME           TEXT    NULL";

	private String createTableExerciseStmt = "CREATE TABLE "+Exercise.class.getSimpleName()+" " +
			"(ID INTEGER PRIMARY KEY  NOT NULL," +
			" NAME           TEXT    NULL,latest_progress int default 1,highest_progress int default 1);";

	private String createTableExercisePosture = "CREATE TABLE "+Exercise.class.getSimpleName()+Posture.class.getSimpleName()+
			"(EXERCISE_ID INT  NOT NULL,"+
			"POSTURE_ID INT  NOT NULL,"
			+ "PRIMARY KEY (EXERCISE_ID, POSTURE_ID));";

	public DAO(){}

	public Exercise[] getExercises(Class<Exercise> ent) {
		Connection c = getConnection();
		Statement stmt;
		Exercise[] list = null;
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT * FROM "+ent.getSimpleName()+";" );
			if(rs.getFetchSize()== 0){
				return null;
			}
			System.out.println(rs.next());
			System.out.println(rs.getFetchSize()+" ");
			list = new Exercise[rs.getFetchSize()];
			int counter = 0;
			while ( rs.next() ) {
				list[counter] = new Exercise();					
				list[counter].setId(rs.getLong("ID"));
				list[counter].setName(rs.getString("NAME"));
				counter++;
			}
			rs.close();
			stmt.close();

		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			throw new RuntimeException(e);
		}
		return list;
	}

	private Connection getConnection() {
		try {
			if(c == null || c.isClosed()){

				Class.forName("org.sqlite.JDBC");
				c = DriverManager.getConnection(connectionString);
				System.out.println("Opened database successfully");
			}else{
				return c;
			}
		} catch (SQLException e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			throw new RuntimeException(e);
		}
		return c;
	}

	/**
	 * Creates 3 tables
	 */
	public void initiateDb(){
		createTables();
	}

	public static void main(String[] args){
		DAO dao = new DAO();
		dao.dropTable(Posture.class.getSimpleName());
		dao.dropTable(Exercise.class.getSimpleName());
		dao.dropTable(Exercise.class.getSimpleName()+Posture.class.getSimpleName());
		dao.initiateDb();
		Posture p = new Posture();
		p.setName("Bend");
		Posture p1 = new Posture("Pose1");
		Posture p2 = new Posture("Pose2");
		Posture p3 = new Posture("Pose3");
		Posture p4 = new Posture("Pose4");
		//dao.delete(p);
		Exercise e = new Exercise();
		e.setName("Test exercise");
		e.addPosture(p1);
		e.addPosture(p2);
		e.addPosture(p3);
		e.addPosture(p4);
		dao.create(e);
		Exercise e1 = new Exercise();
		e1.setId(e.getId());
		dao.read(e1);
		Entity[] eList = dao.getList(Exercise.class.getSimpleName());
		System.out.println("Stored entities:"+e1.getPostures().size()+":"+((Exercise)eList[0]).getPostures().size()+" Expected Entites:"+e.getPostures().size());
		p1.setId(p.getId());
		p1 = dao.read(p1);
		dao.read(p);
		dao.dropTable(Posture.class.getSimpleName());
		dao.dropTable(Exercise.class.getSimpleName());
		dao.dropTable(Exercise.class.getSimpleName()+Posture.class.getSimpleName());
		dao.initiateDb();
	}

	private void dropTable(String string) {
		String tableName = string;	
		Connection c = getConnection();
		Statement stmnt = null;
		String sql = "DROP TABLE "+tableName+";";
		try {
			stmnt = c.createStatement();
			stmnt.execute(sql);
			stmnt.close();
			System.out.println("Table succesfully droped:"+tableName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createTables() {		
		Connection c = getConnection();
		Statement stmnt = null;
		try {
			c.setAutoCommit(false);
			stmnt = c.createStatement();
			String createQueryPostureTbl =createTablePostureStmnt;
			for(JOINT_TAG tag: JOINT_TAG.values()){
				createQueryPostureTbl = createQueryPostureTbl+"," + tag.name()+"X DOUBLE,"+ tag.name()+"Y DOUBLE," +
						tag.name()+"Z DOUBLE";
				System.out.println(createQueryPostureTbl);
			}
			System.out.println("END");
			//createTablePostureStmnt = createTablePostureStmnt+");";
			createTablePostureStmnt ="CREATE TABLE Posture (ID INTEGER PRIMARY KEY NOT NULL,latest_progress INTEGER DEFAULT 1,highest_progress INTEGER DEFAULT 1, duration INTEGER DEFAULT 15, NAME           TEXT    NULL,HEADX DOUBLE,HEADY DOUBLE,HEADZ DOUBLE,NECKX DOUBLE,NECKY DOUBLE,NECKZ DOUBLE,SHOULDER_LEFTX DOUBLE,SHOULDER_LEFTY DOUBLE,SHOULDER_LEFTZ DOUBLE,SHOULDER_RIGHTX DOUBLE,SHOULDER_RIGHTY DOUBLE,SHOULDER_RIGHTZ DOUBLE,SPINE_SHOULDERX DOUBLE,SPINE_SHOULDERY DOUBLE,SPINE_SHOULDERZ DOUBLE,ELBOW_LEFTX DOUBLE,ELBOW_LEFTY DOUBLE,ELBOW_LEFTZ DOUBLE,ELBOW_RIGHTX DOUBLE,ELBOW_RIGHTY DOUBLE,ELBOW_RIGHTZ DOUBLE,HAND_LEFTX DOUBLE,HAND_LEFTY DOUBLE,HAND_LEFTZ DOUBLE,HAND_RIGHTX DOUBLE,HAND_RIGHTY DOUBLE,HAND_RIGHTZ DOUBLE,WRIST_LEFTX DOUBLE,WRIST_LEFTY DOUBLE,WRIST_LEFTZ DOUBLE,WRIST_RIGHTX DOUBLE,WRIST_RIGHTY DOUBLE,WRIST_RIGHTZ DOUBLE,SPINEX DOUBLE,SPINEY DOUBLE,SPINEZ DOUBLE,HIP_CENTERX DOUBLE,HIP_CENTERY DOUBLE,HIP_CENTERZ DOUBLE,HIP_LEFTX DOUBLE,HIP_LEFTY DOUBLE,HIP_LEFTZ DOUBLE,HIP_RIGHTX DOUBLE,HIP_RIGHTY DOUBLE,HIP_RIGHTZ DOUBLE,KNEE_LEFTX DOUBLE,KNEE_LEFTY DOUBLE,KNEE_LEFTZ DOUBLE,KNEE_RIGHTX DOUBLE,KNEE_RIGHTY DOUBLE,KNEE_RIGHTZ DOUBLE,ANKLE_LEFTX DOUBLE,ANKLE_LEFTY DOUBLE,ANKLE_LEFTZ DOUBLE,ANKLE_RIGHTX DOUBLE,ANKLE_RIGHTY DOUBLE,ANKLE_RIGHTZ DOUBLE,FOOT_LEFTX DOUBLE,FOOT_LEFTY DOUBLE,FOOT_LEFTZ DOUBLE,FOOT_RIGHTX DOUBLE,FOOT_RIGHTY DOUBLE,FOOT_RIGHTZ DOUBLE,HAND_TIP_LEFTX DOUBLE,HAND_TIP_LEFTY DOUBLE,HAND_TIP_LEFTZ DOUBLE,HAND_TIP_RIGHTX DOUBLE,HAND_TIP_RIGHTY DOUBLE,HAND_TIP_RIGHTZ DOUBLE,THUMB_LEFTX DOUBLE,THUMB_LEFTY DOUBLE,THUMB_LEFTZ DOUBLE,THUMB_RIGHTX DOUBLE,THUMB_RIGHTY DOUBLE,THUMB_RIGHTZ DOUBLE,SPINE_MIDX DOUBLE,SPINE_MIDY DOUBLE,SPINE_MIDZ DOUBLE,SPINE_BASEX DOUBLE,SPINE_BASEY DOUBLE,SPINE_BASEZ DOUBLE);";
			System.out.println(createTablePostureStmnt);
			System.out.println("Table created:"+createTablePostureStmnt+" Affected rows:"+stmnt.executeUpdate(createTablePostureStmnt));	
			System.out.println("Table created:"+createTableExerciseStmt+" Affected rows:"+stmnt.executeUpdate(createTableExerciseStmt));
			System.out.println("Table created:"+createTableExercisePosture+" Affected rows:"+stmnt.executeUpdate(createTableExercisePosture));
			c.commit();
			stmnt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Exercise create(Exercise ex){
		Connection c = getConnection();
		PreparedStatement prepStmt = null;
		StringBuffer sb = new StringBuffer();
		if(ex.getId()>-1){
			System.out.println("Exercise update initiated ex.id="+ex.getId());
			update(ex);
		}else{
			try {
				c.setAutoCommit(false);
				sb.append("INSERT INTO "+Exercise.class.getSimpleName()+" (NAME,highest_progress,latest_progress)"); 

				sb.append(" VALUES('"+ex.getName()+"',"+ex.getHighestProgress()+","+ex.getLatestProgress()+");");
				prepStmt = c.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
				System.out.println("Create new Ex entity:"+prepStmt.execute()+" Current id:"+ex.getId());
				ResultSet rs = prepStmt.getGeneratedKeys();
				if(rs.next()){
					ex.setId(rs.getLong(1));	
					System.out.println("Returned id"+ex.getId());
				}
				c.commit();
				prepStmt.close();


				List<Posture> postures = ex.getPostures();
				for(Posture p:postures){
					p.setExerciseId(ex.getId());
					create(p);
					createRelationExercisePose(p, ex);
				}
			} catch ( Exception e ) {
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				e.printStackTrace();
			}
		}
		System.out.println("Records created successfully");
		return ex;
	}


	public Exercise read(Exercise ex) {
		Connection c = getConnection();
		Statement stmt;
		try {
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT * FROM "+
					Exercise.class.getSimpleName()+" where ID ="+ex.getId()+";" );
			while ( rs.next() ) {
				ex.setName(rs.getString("NAME"));
				ex.setId(rs.getLong("ID"));
				ex.setHighestProgress(rs.getInt("highest_progress"));
				ex.setLatestProgress(rs.getInt("latest_progress"));
			}
			rs.close();
			stmt.close();
			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM "+
					Exercise.class.getSimpleName()+Posture.class.getSimpleName()+
					" WHERE EXERCISE_ID="+ex.getId()+";");
			while(rs.next()){
				Posture p = new Posture();
				p.setId(rs.getLong("POSTURE_ID"));p.setExerciseId(ex.getId());
				read(p);
				ex.addPosture(p);
			}


		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			throw new RuntimeException(e);
		}
		return ex;
	}

	public Exercise update(Exercise ex) {
		Connection c = getConnection();
		Statement stmnt = null;
		String sql = "UPDATE "+Exercise.class.getSimpleName()+" set NAME='"+ex.getName()+"',"
				+ "highest_progress="+ex.getHighestProgress()+",latest_progress="+ex.getLatestProgress()
				+" where ID="+ex.getId()+";";
		String sqlGetPostures = "SELECT * FROM "+Exercise.class.getSimpleName()+Posture.class.getSimpleName()+
				" WHERE EXERCISE_ID="+ex.getId()+";";

		try {
			c.setAutoCommit(false);
			stmnt = c.createStatement();
			List<Long> ids1 = new LinkedList<>();
			List<Long> ids = new LinkedList<>();
			int counter = 0;
			ResultSet rs = stmnt.executeQuery(sqlGetPostures);
			while(rs.next()){
				ids.add(rs.getLong("POSTURE_ID"));
				ids1.add(ids.get(counter++));
			}
			System.out.println("Exercise is being updated:"+ex.getId()+" rows modified:"+	stmnt.executeUpdate(sql));

			for(Posture p:ex.getPostures()){
				if(p.getId() < 0){
					create(p);
					createRelationExercisePose(p, ex);
				}else{
					update(p);
					for(Long id:ids){
						if(id == p.getId()){
							ids1.remove(id);
							System.out.println("List size:"+ids1.size());
						}
					}
				}

			}
			/* Remove elements that didn't appear in exercise list */
			for(Long id:ids1){
				deleteRelationExercisePose(ex,id);
			}
			c.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ex;
	}

	private void deleteRelationExercisePose(Exercise ex, Long id) {
		Statement stmt = null;
		Connection c = getConnection();
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
			stmt.executeUpdate("DELETE FROM "+Exercise.class.getSimpleName()+Posture.class.getSimpleName()+
					" WHERE EXERCISE_ID="+ex.getId()+" AND POSTURE_ID="+id+";");
			c.commit();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void delete(Exercise ex){
		Connection c = getConnection();
		Statement stmt = null;
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "DELETE from "+Exercise.class.getSimpleName()+" where ID="+ex.getId()+";";
			stmt.execute(sql);
			c.commit();
			stmt.close();
			deleteRelationExercisePose(ex);
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			throw new RuntimeException(e);
		}
		System.out.println("Operation done successfully");
	}

	public Posture create(Posture p){
		if(p.getId() >-1){
			update(p);
			return p;
		}
		Connection c = getConnection();
		PreparedStatement stmt = null;
		StringBuffer sb = new StringBuffer();
		try {
			c.setAutoCommit(false);
			sb.append("INSERT INTO "+Posture.class.getSimpleName()+"(NAME,duration,latest_progress,highest_progress,"); 
			Set<String> keySet = p.getJointMap().keySet();
			Map<String,double[]> map = p.getJointMap();
			for(String key:keySet){
				sb.append(key+"X,");sb.append(key+"Y,");sb.append(key+"Z,");
			}
			sb.setCharAt(sb.length()-1, ')');
			sb.append(" VALUES('"+p.getName()+"',"+p.getDuration()+","+p.getLatestProgress()+","
					+p.getHighestProgress()+",");
			for(String key:keySet){
				double[] arr = map.get(key);
				sb.append(arr[0]+","+arr[1]+","+arr[2]+",");
			}
			sb.setCharAt(sb.length()-1, ')');
			sb.append(";");
			stmt = c.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
			stmt.execute();


			ResultSet rs = stmt.getGeneratedKeys();
			if(rs.next()){
				p.setId(rs.getLong(1));
			}else{
				throw new RuntimeException("Keyset is empty.");
			}


			c.commit();
			stmt.close();

		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			e.printStackTrace();
			return null;
		}
		System.out.println("Records created successfully");
		return p;
	}



	/**
	 * Method extracts id and returns entity under given ID, else returns null
	 * @param p
	 * @return
	 */
	public Posture read(Posture p) {
		Connection c = getConnection();
		Statement stmt;
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT * FROM "+Posture.class.getSimpleName()+" WHERE ID="+p.getId()+";" );
			while ( rs.next() ) {
				if(p.getId() == rs.getLong("ID")){
					System.out.println("ID="+rs.getLong("ID")+"Expected ID="+p.getId());
					p.setName(rs.getString("NAME"));
					for(String key:p.getJointMap().keySet()){
						p.setJoinLocation(key, new double[]{rs.getDouble(key+"X"),rs.getDouble(key+"Y"),rs.getDouble(key+"Z")});
					}
				}else{
					System.out.println("ID="+rs.getLong("ID")+"Expected ID="+p.getId());
				}
			}
			rs.close();
			stmt.close();

		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			throw new RuntimeException(e);
		}
		return p;
	}

	public Posture update(Posture pose) {
		Connection c = getConnection();
		Statement stmnt = null;
		String sql = "UPDATE "+ Posture.class.getSimpleName() +" set NAME='"+pose.getName()+"',"
				+" latest_progress="+pose.getLatestProgress()+
				", highest_progress="+pose.getHighestProgress()+",duration="+pose.getDuration()+",";

		StringBuilder sb = new StringBuilder(sql);
		Set<String> set =pose.getJointMap().keySet();
		Map<String,double[]> map = pose.getJointMap();

		for(String tag:set){
			sb.append(tag +"X="+map.get(tag)[0]+","+tag+"Y="+map.get(tag)[1]+","+tag +"Z="+map.get(tag)[2]+",");
		}
		sb.setCharAt(sb.length()-1, ' ');
		sb.append("WHERE ID="+pose.getId());

		System.out.println(sb.toString());
		try {
			c.setAutoCommit(false);
			stmnt = c.createStatement();
			stmnt.executeUpdate(sb.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pose;
	}

	public void delete(Posture p){
		Connection c = getConnection();
		Statement stmt = null;
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
			String sql = "DELETE from "+Posture.class.getSimpleName()+" where ID="+p.getId()+";";
			stmt.execute(sql);
			c.commit();
			stmt.close();

		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			throw new RuntimeException(e);
		}
		System.out.println("Operation done successfully");
	}

	public void createRelationExercisePose(Posture p,Exercise ex){
		Connection c = getConnection();
		Statement stmt = null;
		StringBuffer sb = new StringBuffer();
		try {
			c.setAutoCommit(false);
			sb.append("INSERT INTO "+Exercise.class.getSimpleName()+Posture.class.getSimpleName()+"(EXERCISE_ID,POSTURE_ID)"); 
			sb.append(" VALUES("+ex.getId()+","+p.getId()+");");
			stmt = c.createStatement();
			stmt.executeUpdate(sb.toString());
			c.commit();
			stmt.close();

		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			e.printStackTrace();
		}
		System.out.println("Records created successfully");
	}



	public Posture readRTableExercisePose(Posture p, Exercise ex) {
		Connection c = getConnection();
		Statement stmt;
		try {
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT * FROM "+Exercise.class.getSimpleName()+Posture.class.getSimpleName()+" where ID ="+p.getId()+";" );
			while ( rs.next() ) {
				p.setName(rs.getString("NAME"));
				for(String key:p.getJointMap().keySet()){
					p.setJoinLocation(key, new double[]{rs.getDouble(key+"X"),rs.getDouble(key+"Y"),rs.getDouble(key+"Z")});
				}
			}
			rs.close();
			stmt.close();

		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			throw new RuntimeException(e);
		}
		return p;
	}

	public void updateRTableExercisePose(Exercise ex,Posture pose) {
		throw new RuntimeException("Not implemented");

	}

	public void deleteRelationExercisePose(Exercise ex){
		Connection c = getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			c.setAutoCommit(false);
			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM "+Exercise.class.getSimpleName()+Posture.class.getSimpleName()+
					" WHERE EXERCISE_ID="+ex.getId());
			while(rs.next()){
				stmt.executeUpdate("DELETE FROM "+Posture.class.getSimpleName()+
						" WHERE ID="+rs.getLong("POSTURE_ID"));
			}
			String sql = "DELETE from "+Exercise.class.getSimpleName()+Posture.class.getSimpleName()+" where EXERCISE_ID="+ex.getId()+";";
			stmt.execute(sql);
			c.commit();
			stmt.close();

		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			throw new RuntimeException(e);
		}
		System.out.println("Operation done successfully");
	}



	public Entity[] getList(String className) {
		Connection c = getConnection();
		Statement stmt;
		Entity[] list = null;
		try {
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT * FROM "+className+";" );
			if(Posture.class.getSimpleName().equals(className)){
				LinkedList<Posture> lList = new LinkedList<>();
				while ( rs.next() ) {
					Posture p = new Posture();
					lList.add(p);	
					Set<String> keySet = p.getJointMap().keySet();
					for(String key:keySet){
						p.setJoinLocation(key,new double[]{rs.getDouble(key+"X"),rs.getDouble(key+"Y"),rs.getDouble(key+"Z")});
					}
					p.setId(rs.getLong("ID"));p.setName(rs.getString("NAME"));
				}
				rs.close();
				list = new Posture[lList.size()];
				lList.toArray(list);
			}else if(Exercise.class.getSimpleName().equals(className)){
				LinkedList<Exercise> lList = new LinkedList<>();
				while ( rs.next() ) {
					Exercise e = new Exercise();
					lList.add(e);	
					e.setId(rs.getLong("ID"));
					read(e);
					System.out.println("Loaded exercise ob:"+e.getId());
				}
				rs.close();
				list = new Exercise[lList.size()];
				lList.toArray(list);

			}
			rs.close();
			stmt.close();

		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			throw new RuntimeException(e);
		}
		return list;
	}
	@Override
	public Entity create(Entity e) {
		if(e instanceof Exercise){
			return create((Exercise)e);
		}else if(e instanceof Posture){
			return create((Posture)e);
		}else{
			throw new RuntimeException("Unrecognized entity");
		}
	}
	@Override
	public Entity read(Entity e) {
		if(e instanceof Exercise){
			return read((Exercise)e);
		}else if(e instanceof Posture){
			return read((Posture)e);
		}else{
			throw new RuntimeException("Unrecognized entity");
		}
	}
	@Override
	public Entity update(Entity e) {
		if(e instanceof Exercise){
			return update((Exercise)e);
		}else if(e instanceof Posture){
			return update((Posture)e);
		}else{
			throw new RuntimeException("Unrecognized entity");
		}
	}
	@Override
	public void delete(Entity e) {
		if(e instanceof Exercise){
			delete((Exercise)e);
		}else if(e instanceof Posture){
			delete((Posture)e);
		}else{
			throw new RuntimeException("Unrecognized entity");
		}		
	}

}
