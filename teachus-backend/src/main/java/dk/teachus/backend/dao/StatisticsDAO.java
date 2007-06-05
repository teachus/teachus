package dk.teachus.backend.dao;

import java.util.List;

import dk.teachus.backend.domain.TeacherStatistics;

public interface StatisticsDAO {

	List<TeacherStatistics> getTeachers();
	
}
