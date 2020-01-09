package com.ysmind.modules.sys.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ysmind.common.service.BaseService;
import com.ysmind.exception.UncheckedException;
import com.ysmind.modules.sys.dao.DictDao;
import com.ysmind.modules.sys.entity.Dict;

/**
 * 字典Service
 * @version 2013-5-29
 */
@Service
@Transactional(readOnly = true)
public class DictService extends BaseService {

	@Autowired
	private DictDao dictDao;
	
	
	public Dict get(String id) {
		return dictDao.get(id);
	}
	
	
	public List<String> findTypeList(){
		return dictDao.findTypeList();
	}
	
	public List<Dict> findByParentId(String parentId){
		return dictDao.findByParentId(parentId);
	}
	
	@Transactional(readOnly = false)
	public void save(Dict dict, HttpServletRequest request) throws Exception{
		
		dict.setParent(this.get(dict.getParent().getId()));
		String oldParentIds = dict.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
		dict.setParentIds(dict.getParent().getParentIds()+dict.getParent().getId()+",");
		dictDao.clear();
		dictDao.save(dict);
		// 更新子节点 parentIds
		List<Dict> list = dictDao.findByParentIdsLike("%,"+dict.getId()+",%");
		for (Dict e : list){
			e.setParentIds(e.getParentIds().replace(oldParentIds, dict.getParentIds()));
		}
		dictDao.save(list);
		dictDao.flush();
		//WorkflowFormUtils.dealAttach(request, dict.getId());
		//int j = 1/0;
		//CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		dictDao.deleteById(id);
		//CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}
	
	@Transactional(readOnly = false)
	public void deleteSelectedIds(String ids) {
		List<Object> list = new ArrayList<Object>();
		list.add(Dict.DEL_FLAG_DELETE);
		dictDao.deleteDicts(dealIds(ids,":",list));
		//CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}
	
	public List<Dict> findListByType(String type){
		return dictDao.findListByType(type);
	}
	
	@Transactional(readOnly = false)
	public List<Map<String, Object>> multiSelectData() {
		return dictDao.multiSelectData();
	}
	
	@SuppressWarnings("unused")
	@Transactional(readOnly = false)
	public void testAjax() throws Exception{
		try {
			Dict dict =  new Dict();
			dict.setType("902");
			dict.setLabel("902");
			dict.setValue("902");
			dict.setDescription("902");
			dict.setSort(902);
			dictDao.save(dict);
			int a = 0,b=1;
			int c = b/a;
		} catch (Exception e) {
			System.out.println("-----------DictService>testAjax-----------");
			throw new UncheckedException("DictService>testAjax>error[1001]",new RuntimeException());
		}
	}
	
	/**
	 * 下面这些异常也可以在dao层或util类中抛出
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	@Transactional(readOnly = false)
	public void testNormalRequest() throws Exception{
		try {
			Dict dict =  new Dict();
			dict.setType("777");
			dict.setLabel("777");
			dict.setValue("777");
			dict.setDescription("777");
			dict.setSort(777);
			dictDao.save(dict);
			int a = 0,b=1;
			int c = b/a;
		} catch (Exception e) {
			System.out.println("-----------DictService>testNormalRequest-----------");
			throw new UncheckedException("DictService>testAjax>testNormalRequest[9009]",new RuntimeException());
		}
	}
	
}
