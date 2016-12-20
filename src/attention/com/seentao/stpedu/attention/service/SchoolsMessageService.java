package com.seentao.stpedu.attention.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.seentao.stpedu.common.components.QueryPage;
import com.seentao.stpedu.common.components.QueryPageComponent;
import com.seentao.stpedu.common.entity.PublicRegion;
import com.seentao.stpedu.common.entity.TeachSchool;
import com.seentao.stpedu.common.service.PublicRegionService;
import com.seentao.stpedu.constants.GameConstants;
import com.seentao.stpedu.error.AppErrorCode;
import com.seentao.stpedu.utils.LogUtil;

/**
 * 学校信息 
 * <pre>项目名称：stpedu    
 * 类名称：SchoolsMessageService    
 */
@Service
public class SchoolsMessageService {

	@Autowired
	private PublicRegionService  publicRegionService;
	
	/**
	 * getSchools(获取学校信息)
	 * @param userId 用户ID
	 * @param start 起始数
	 * @param limit 每页数量 
	 * @param provinceId 省份id
	 * @param cityId 城市ID
	 * @param sortType 排序类型 1:人数；2:学时；3:实训；4:原创；
	 * @param inquireType 1:教学模块，学校列表页调用   2:俱乐部模块，创建俱乐部页面的选择学校时调用
	 * @author ligs
	 * @date 2016年6月29日 下午3:57:42
	 * @return
	 */
	public JSONObject getSchools(Integer userId, Integer start, Integer limit, Integer provinceId, Integer cityId,Integer sortType, Integer inquireType) {
		if(inquireType == GameConstants.GET_CLASS_MESSAGE_ONE){//1：教学模块，学校列表页调用
			//得到学校省级名称
			/*获取查询条件
			 * 0.如果省份id为空，并且城市也为空。不查
			 * 1.如果省份id不为空，并且城市id为空。查出所有的城市ids
			 * 2.如果省份id不为空，城市id不为空。查询城市单个id
			 */
			/*
			 *根据条件查询地区表 
			 *
			 */
			StringBuffer sb = new StringBuffer();
			PublicRegion publicRegion = null;
			List<PublicRegion> regionList = null;
			String ids = null;
			//省份参数添加
			if(provinceId != null && provinceId > 0){
				publicRegion = new PublicRegion();
				publicRegion.setParentId(provinceId);
			}
			//城市参数添加
			if(publicRegion != null && cityId != null && cityId > 0){
				publicRegion.setRegionId(cityId);
			}
			//查询所有的城市id
			if(publicRegion != null ){
				regionList =  publicRegionService.getPublicRegionList(publicRegion);
			}
			//获取筛选城市ids
			if(!CollectionUtils.isEmpty(regionList)){
				for(PublicRegion en : regionList){
					sb.append(en.getRegionId()).append(",");
				}
				ids = sb.toString();
				ids = ids.substring(0, ids.length()-1);
			}
			Map<String ,Object> paramMap = new HashMap<String ,Object>();
			//获取查询条件 城市id
			if(ids != null){
				paramMap.put("regionId", ids);
			}
			//页面显示类型
			if(sortType == 1){//人数排序
				paramMap.put("orderBy", "studentNum");
			}else if(sortType == 2){//学时排序
				paramMap.put("orderBy", "learningSeconds");
			}else if(sortType == 3){//实训排序
				paramMap.put("orderBy", "practiceCount");
			}else if(sortType == 4){//原创排序
				paramMap.put("orderBy", "courseCount");
			}
			QueryPage<TeachSchool> appQueryPage = QueryPageComponent.queryPage(limit, start, paramMap, TeachSchool.class);
			//msg错误码
			if(!appQueryPage.getState()){
				LogUtil.error(this.getClass(), "getSchools", "获取学校列表失败");
				appQueryPage.error(AppErrorCode.GET_CLASS_LIST_ERROR);
			}
			for (TeachSchool publicRegion2 : appQueryPage.getList()) {
				publicRegion2.setStrSchoolId(String.valueOf(publicRegion2.getSchoolId()));
			}
			LogUtil.info(this.getClass(), "getSchools", "获取学校列表成功");
			return appQueryPage.getMessageJSONObject("schools");
		}else if(inquireType == GameConstants.GET_CLASS_MESSAGE_TWO){//2:俱乐部模块，创建俱乐部页面的选择学校时调用
			if(cityId == null){//如果城市ID为空 通过省份ID查询地区表获得省份下所有地区ID
				PublicRegion publicRegion = new PublicRegion();
				publicRegion.setParentId(provinceId);
				List<PublicRegion> regionList =  publicRegionService.getPublicRegionList(publicRegion);
				//通过学校所在省ID获得省名称
				PublicRegion publicRegions = new PublicRegion();
				publicRegions.setParentId(provinceId);
				PublicRegion publicRegionAll = publicRegionService.getPublicRegion(publicRegions);
				//遍历list得到地区ID  通过地区ID查询学校表
				for (PublicRegion selectpublicRegio : regionList) {
					Map<String ,Object> paramMap = new HashMap<String ,Object>();
					paramMap.put("regionId", selectpublicRegio.getRegionId());
					//调用分页组件
					QueryPage<TeachSchool> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, TeachSchool.class,"querySchoolCount","querySchoolByPage");
					//msg错误码
					if(!appQueryPage.getState()){
						LogUtil.error(this.getClass(), "getSchools", "获取学校列表失败");
						appQueryPage.error(AppErrorCode.GET_CLASS_LIST_ERROR);
					}
					for(TeachSchool teachreturn:appQueryPage.getList()){
						teachreturn.setProvinceId(provinceId);//所在省ID
						teachreturn.setProvinceName(publicRegionAll.getRegionName());//学校所在省名称
						teachreturn.setStrSchoolId(String.valueOf(teachreturn.getSchoolId()));
					}
					LogUtil.info(this.getClass(), "getSchools", "获取学校列表成功");
					return appQueryPage.getMessageJSONObject("schools");
				}
			}else if(cityId != null){
				//通过城市上级ID查询地区表获取省级名称
				PublicRegion publicRegion = new PublicRegion();
				publicRegion.setRegionId(provinceId);
				PublicRegion publicRegionName = publicRegionService.getPublicRegion(publicRegion);
				//遍历list得到地区ID  通过地区ID查询学校表
					Map<String ,Object> paramMap = new HashMap<String ,Object>();
					paramMap.put("regionId", cityId);
					//调用分页组件
					QueryPage<TeachSchool> appQueryPage = QueryPageComponent.queryPageExecute(limit, start, paramMap, TeachSchool.class,"querySchoolCount","querySchoolByPage");
					//msg错误码
					if(!appQueryPage.getState()){
						LogUtil.error(this.getClass(), "getSchools", "获取学校列表失败");
						appQueryPage.error(AppErrorCode.GET_CLASS_LIST_ERROR);
					}
					for(TeachSchool teachreturn:appQueryPage.getList()){
						teachreturn.setProvinceId(provinceId);//所在省ID
						teachreturn.setProvinceName(publicRegionName.getRegionName());//学校所在省名称
						teachreturn.setStrSchoolId(String.valueOf(teachreturn.getSchoolId()));
					}
					LogUtil.info(this.getClass(), "getSchools", "获取学校列表成功");
					return appQueryPage.getMessageJSONObject("schools");
			}
		}
		return null;
	}

}
