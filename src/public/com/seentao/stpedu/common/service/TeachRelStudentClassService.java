package com.seentao.stpedu.common.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.Common;
import com.seentao.stpedu.common.components.RedisComponent;
import com.seentao.stpedu.common.dao.TeachRelStudentClassDao;
import com.seentao.stpedu.common.entity.CenterUser;
import com.seentao.stpedu.common.entity.PublicPicture;
import com.seentao.stpedu.common.entity.TeachRelStudentClass;
import com.seentao.stpedu.common.entity.User;
import com.seentao.stpedu.config.ActiveUrl;
import com.seentao.stpedu.constants.BusinessConstant;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.redis.JedisCache;
import com.seentao.stpedu.utils.LogUtil;
import com.seentao.stpedu.utils.StringUtil;

@Service
public class TeachRelStudentClassService{
	
	@Autowired
	private TeachRelStudentClassDao teachRelStudentClassDao;
	
	public TeachRelStudentClass getTeachRelStudentClass(TeachRelStudentClass teachRelStudentClass) {
		List<TeachRelStudentClass> teachRelStudentClassList = teachRelStudentClassDao .selectSingleTeachRelStudentClass(teachRelStudentClass);
		if(teachRelStudentClassList == null || teachRelStudentClassList .size() <= 0){
			return null;
		}
		return teachRelStudentClassList.get(0);
	}
	
	public List<TeachRelStudentClass> getTeachRelStudentClassList(TeachRelStudentClass teachRelStudentClass) {
		List<TeachRelStudentClass> teachRelStudentClassList = teachRelStudentClassDao .selectSingleTeachRelStudentClass(teachRelStudentClass);
		if(teachRelStudentClassList == null || teachRelStudentClassList .size() <= 0){
			return null;
		}
		return teachRelStudentClassList;
	}
	
	
	public TeachRelStudentClass selectTeachRelStudentClass(TeachRelStudentClass teachRelStudentClass){
		return teachRelStudentClassDao .selectTeachRelStudentClass(teachRelStudentClass);
	}
	
	public void insertTeachRelStudentClass(TeachRelStudentClass teachRelStudentClass){
		teachRelStudentClassDao .insertTeachRelStudentClass(teachRelStudentClass);
	}

	/**
	 * 
	 * saveStudentAll(Map批量添加)   
	 * @author ligs
	 * @date 2016年6月22日 下午5:53:16
	 * @return
	 */
	public void saveStudentAll(List<TeachRelStudentClass> classes) {
		teachRelStudentClassDao.saveStudentAll(classes);
	}


	public void deleteTeachStudentAll(List<TeachRelStudentClass> teachStudentAll)throws Exception{
		teachRelStudentClassDao.deleteTeachStudentAll(teachStudentAll);
	}
	
	public void updateTeachRelStudentClassByKey(TeachRelStudentClass teachRelStudentClass){
		teachRelStudentClassDao .updateTeachRelStudentClassByKey(teachRelStudentClass);
	}


	/**
	 * 更新学生班级关系表，删除缓存
	 * @param selectBean	保存bean
	 * @return
	 * @author 				lw
	 * @date				2016年6月22日  下午5:33:19
	 */
	public JSONObject saveOrUpdate(TeachRelStudentClass save) {
	
		//1.保存平分兑现
		if(save.getRelId() == null){
			this.insertTeachRelStudentClass(save);
			LogUtil.info(this.getClass(), "saveOrUpdate", String.valueOf(GameConstants.SUCCESS_INSERT));
			
		//2.修改平分
		}else{
			this.updateTeachRelStudentClassByKey(save);
			LogUtil.info(this.getClass(), "saveOrUpdate", String.valueOf(GameConstants.SUCCESS_UPDATE));
			//删除redis上缓存
			JedisCache.delRedisVal(TeachRelStudentClass.class.getSimpleName(), String.valueOf(save.getRelId()));
		}
		return Common.getReturn(AppErrorCode.SUCCESS, "操作成功!");
		
	}


	/**
	 * 添加认证资格
	 * @param classId		班级id
	 * @param studentId		学生id
	 * @return
	 * @author 				lw
	 * @date				2016年6月22日  下午5:35:19
	 */
	public JSONObject addAuthentication(Integer classId, int certObjectId) {
		TeachRelStudentClass findUserBean = this.findUserBean( classId, certObjectId);
		findUserBean.setIsIdentify(GameConstants.YES);
		return this.saveOrUpdate(findUserBean);
	}
	
	/**
	 * 获取数据对象
	 * @param classId		课程
	 * @param certObjectId	认证用户对象id
	 * @return
	 * @author 			lw
	 * @date			2016年6月23日  下午10:17:50
	 */
	private TeachRelStudentClass findUserBean(Integer classId, int certObjectId) {
		TeachRelStudentClass tmpdb = new TeachRelStudentClass();
		tmpdb.setClassId(classId);
		tmpdb.setStudentId(certObjectId);
		tmpdb.setIsDelete(GameConstants.NO_DEL);
		tmpdb = this.selectTeachRelStudentClass(tmpdb);
		if(tmpdb == null){
			tmpdb = new TeachRelStudentClass();
			tmpdb.setClassId(classId);
			tmpdb.setStudentId(certObjectId);
			tmpdb.setIsDelete(GameConstants.NO_DEL);
		}
		return tmpdb;
	}


	/**
	 * 取消认证资格
	 * @param classId		班级id
	 * @param studentId		学生id
	 * @return
	 * @author 				lw
	 * @date				2016年6月22日  下午5:35:19
	 */
	public JSONObject delAuthentication(Integer classId, int certObjectId) {
		TeachRelStudentClass entity = this.findUserBean( classId, certObjectId);
		entity.setIsIdentify(GameConstants.NO);
		return this.saveOrUpdate(entity);
	}
	
	/**
	 * 申请认证资格
	 * @param classId		班级id
	 * @param studentId		学生id
	 * @return
	 * @author 				lw
	 * @date				2016年6月22日  下午5:35:19
	 */
	public JSONObject applyAuthentication(Integer classId, int certObjectId) {
		TeachRelStudentClass entity = this.findUserBean( classId, certObjectId);
		entity.setCertificateStatus(GameConstants.YES);
		return this.saveOrUpdate(entity);
	}
	
	/**
	 * 根据班级id查询成绩排名
	 * @param classId
	 * @param start
	 * @param limit
	 * @return
	 */
	public List<User> getGradesByClassId(String classId, Integer start, Integer limit) {
		List<User> res = new ArrayList<User>();
		List<TeachRelStudentClass> list = teachRelStudentClassDao.getGradesByClassId(classId,start,limit);
		if(null!=list && list.size()!=0){
			for (int i = 0; i < list.size(); i++) {
				User user = new User();
				TeachRelStudentClass t = list.get(i);
				String cacheUser = RedisComponent.findRedisObject(t.getStudentId(), CenterUser.class);
				if(!StringUtil.isEmpty(cacheUser)){
					CenterUser c = JSONObject.parseObject(cacheUser,CenterUser.class);
					Integer imgId = c.getHeadImgId()==null?BusinessConstant.DEFAULT_IMG_USER:c.getHeadImgId();
					String cachePublicPicture = RedisComponent.findRedisObject(imgId, PublicPicture.class);
					if(!StringUtil.isEmpty(cachePublicPicture)){//个人头像信息
						PublicPicture publicpicture = JSONObject.parseObject(cachePublicPicture,PublicPicture.class);
						user.setHeadLinkId(String.valueOf(publicpicture.getPicId()));
						user.setHeadLink(Common.checkPic(publicpicture.getDownloadUrl()) == true ? publicpicture.getDownloadUrl()+ActiveUrl.HEAD_MAP:publicpicture.getDownloadUrl());
					}else{
						user.setHeadLinkId("");
						user.setHeadLink("");
					}
					user.setUserId(String.valueOf(t.getStudentId()));//用户信息
					user.setRealName(c.getRealName()==null?"":c.getRealName());
					user.setNickname(c.getNickName()==null?"":c.getNickName());
					user.setDesc(c.getDescription()==null?"":c.getDescription());
				}
				res.add(user);
			}
		}
		return res;
	}
	
	public Integer queryCountByClassId(String classId) {
		return teachRelStudentClassDao.queryCountByClassId(classId);
	}
	
	public void deleteTeachRelStudentClass(TeachRelStudentClass teachRelStudentClass){
		teachRelStudentClassDao.deleteTeachRelStudentClass(teachRelStudentClass);
	}

	public void updateTeachRelStudentClassByKeyNoRel(TeachRelStudentClass teachStudentClass) {
		teachRelStudentClassDao.updateTeachRelStudentClassByKeyNoRel(teachStudentClass);
	}
	
}