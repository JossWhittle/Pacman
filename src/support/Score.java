import java.sql.ResultSet;
import java.sql.SQLException;

public class Score {
		
	// Constants
	
	// Members
	private String m_name, m_date;
	private int m_score;
	
	public Score(String name, String date, int score) {
		m_name = name;
		m_date = date;
		m_score = score;
	}
	
	public Score(ResultSet res) {
		try {
			m_name = res.getString("name");
			m_date = res.getString("date");
			m_score = res.getInt("score");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getName() {
		return m_name;
	}
	
	public String getDate() {
		return m_date;
	}
	
	public int getScore() {
		return m_score;
	}
}