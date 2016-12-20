package com.seentao.stpedu.common.sqlmap;


import java.util.List;
import java.util.Map;

import com.seentao.stpedu.common.entity.TeachStudentHomework;

/**
 * @author lw
 * @date   2016年6月18日 上午9:36:26
 */
public interface TeachStudentHomeworkMapper {

	public abstract void insertTeachStudentHomework(TeachStudentHomework teachStudentHomework);
	
	public abstract void deleteTeachStudentHomework(TeachStudentHomework teachStudentHomework);
	
	public abstract void updateTeachStudentHomeworkByKey(TeachStudentHomework teachStudentHomework);
	
	public abstract List<TeachStudentHomework> selectSingleTeachStudentHomework(TeachStudentHomework teachStudentHomework);
	
	public abstract List<TeachStudentHomework> selectAllTeachStudentHomework();
	
	public abstract Integer queryCount(Map<String,String> paramMap);
	
	public abstract List<TeachStudentHomework> queryByPage(Map<String,String> paramMap);

	public abstract List<TeachStudentHomework> findAllStudentsHomeWorkStudent(String ids);

	public abstract List<TeachStudentHomework> findTeacherInfo(Integer userId);

	public abstract Integer findAllStudentsHomeWorkStudentByCount(Map<String, Object> paramMap);

	public abstract List<TeachStudentHomework> findAllStudentsHomeWorkStudentByPage(Map<String, Object> paramMap);

	public abstract List<Integer> queryByPageids(Map<String, String> paramMap);
}