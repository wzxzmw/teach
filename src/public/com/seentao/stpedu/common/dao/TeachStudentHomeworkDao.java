package com.seentao.stpedu.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.seentao.stpedu.common.entity.TeachStudentHomework;
import com.seentao.stpedu.common.sqlmap.TeachStudentHomeworkMapper;

/**
 * 
 * @author lw
 * @date   2016年6月18日 上午9:36:42
 */
@Repository
public class TeachStudentHomeworkDao {

	@Autowired
	private TeachStudentHomeworkMapper teachStudentHomeworkMapper;
	
	
	public void insertTeachStudentHomework(TeachStudentHomework teachStudentHomework){
		teachStudentHomeworkMapper .insertTeachStudentHomework(teachStudentHomework);
	}
	
	public void deleteTeachStudentHomework(TeachStudentHomework teachStudentHomework){
		teachStudentHomeworkMapper .deleteTeachStudentHomework(teachStudentHomework);
	}
	
	public void updateTeachStudentHomeworkByKey(TeachStudentHomework teachStudentHomework){
		teachStudentHomeworkMapper .updateTeachStudentHomeworkByKey(teachStudentHomework);
	}
	
	public List<TeachStudentHomework> selectSingleTeachStudentHomework(TeachStudentHomework teachStudentHomework){
		return teachStudentHomeworkMapper .selectSingleTeachStudentHomework(teachStudentHomework);
	}
	
	public TeachStudentHomework selectTeachStudentHomework(TeachStudentHomework teachStudentHomework){
		List<TeachStudentHomework> teachStudentHomeworkList = teachStudentHomeworkMapper .selectSingleTeachStudentHomework(teachStudentHomework);
		if(teachStudentHomeworkList == null || teachStudentHomeworkList .size() == 0){
			return null;
		}
		return teachStudentHomeworkList .get(0);
	}
	
	public List<TeachStudentHomework> selectAllTeachStudentHomework(){
		return teachStudentHomeworkMapper .selectAllTeachStudentHomework();
	}

	public Integer queryCount(Map<String, String> paramMap) {
		return teachStudentHomeworkMapper.queryCount(paramMap);
	}
	public List<Integer> queryByPageids(Map<String, String> paramMap) {
		return teachStudentHomeworkMapper.queryByPageids(paramMap);
	}

	public List<TeachStudentHomework> queryByPage(Map<String, String> paramMap) {
		return teachStudentHomeworkMapper.queryByPage(paramMap);
	}
	
	public List<TeachStudentHomework> findAllStudentsHomeWorkStudent(String ids){
		return teachStudentHomeworkMapper.findAllStudentsHomeWorkStudent(ids);
	}
	
	public TeachStudentHomework findTeacherInfo(Integer userId){
		List<TeachStudentHomework> findTeacherInfo = teachStudentHomeworkMapper.findTeacherInfo(userId);
		return findTeacherInfo.get(0);
	}
	
	public Integer findAllStudentsHomeWorkStudentByCount(Map<String, Object> paramMap){
		return teachStudentHomeworkMapper.findAllStudentsHomeWorkStudentByCount(paramMap);
	}
	
	public List<TeachStudentHomework> findAllStudentsHomeWorkStudentByPage(Map<String, Object> paramMap){
		return teachStudentHomeworkMapper.findAllStudentsHomeWorkStudentByPage(paramMap);
	}
	
	/**
	 * redis组件 反射调用获取单表数据
	 * @param paramMap	查询参数
	 * @return
	 * @author 			lw
	 * @date			2016年6月21日  下午7:24:07
	 */
	public TeachStudentHomework getEntityForDB(Map<String, Object> paramMap){
		TeachStudentHomework tmp = new TeachStudentHomework();
		tmp.setCourseId(Integer.valueOf(paramMap.get("id_key").toString()));
		return this.selectTeachStudentHomework(tmp);
	}
}