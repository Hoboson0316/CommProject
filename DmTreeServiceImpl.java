package com.foresee.sx.tycx.hxfw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.foresee.fsdf.annotation.FService;
import com.foresee.sx.tycx.api.IDmTreeService;
import com.foresee.sx.tycx.dao.DmTreeDao;
import com.foresee.sx.tycx.entity.TreeJson;
import com.foresee.sx.tycx.entity.ydjl.GwVO;


/**
 * 
 * @ClassName: DmTreeServiceImpl 
 * @Description: TODO
 * @author: Hoboson
 * @date: 2017年8月27日 下午9:58:39
 */

@FService
public class DmTreeServiceImpl implements IDmTreeService{

	@Autowired
	DmTreeDao dmTreeDao;
	
	@Override
	public List<TreeJson> loadSwjgTree(String swjg) throws Exception {
		String currentswjg="";
		if(null ==swjg || "".equals(swjg)){
			currentswjg="16100000000";	
		}else{
			currentswjg=swjg;
		}
		List<Map<String, String>> dataMapList = dmTreeDao.findSwjgAll();
		// TODO Auto-generated method stub
		return  loadTree(dataMapList,currentswjg,"SJSWJG", "SWJGDM", "SWJGMC");
	}

	
	public List<TreeJson> loadHyTree(){
		List<Map<String, String>> dataMapList = dmTreeDao.findHyAll();
		return loadTree(dataMapList,"","FJFLBH", "HY_DM", "HYMC");
	}
	
	public List<TreeJson> loadZsxmTree(){
		List<Map<String, String>> zsxmList = dmTreeDao.findZsxmAll();
		return loadTree(zsxmList,"","SJZSXM_DM", "ZSXM_DM", "ZSXMMC");
	}
	
	public List<TreeJson> loadDjzclxTree(){
		List<Map<String, String>> zcjlxList = dmTreeDao.findDjzclxAll();
		return loadTree(zcjlxList,"000","SJDJZCLX_DM", "DJZCLX_DM", "DJZCLXMC");
	}

	
	@Override
	public List<TreeJson> loadtsrysgywzhg(GwVO vo) throws Exception {
		List<Map<String, String>> tsrylist = dmTreeDao.loadtsrysgywzhg(vo);
		return loadTree(tsrylist,"","SJSWJG_DM","SWJG_DM","RYSFMC");
	}

	
	@Override
	public List<TreeJson> findTsry(GwVO vo) throws Exception {
		List<Map<String, String>> tsrylist = dmTreeDao.findTsry(vo);
		return loadTree(tsrylist,"","SJSWJG_DM","SWJG_DM","RYSFMC");
	}
	

	@Override
	public List<TreeJson> hcbtsry(String sfdm) throws Exception {
		List<Map<String, String>> tsrylist = dmTreeDao.hcbtsry(sfdm);
		return  loadTree(tsrylist,"","SJSWJG_DM","SWJG_DM","RYSFMC");
	}
	
	
	@Override
	public List<Map<String, String>>  getYdbh(String nsrsbh) throws Exception {
		List<Map<String, String>> lsMaps = dmTreeDao.getYdbh(nsrsbh);
		return lsMaps;
	}

	
	@Override
	public List<Map<String, String>>  getGnmc(String gncode) throws Exception {
		List<Map<String, String>> lsMaps = dmTreeDao.getGnmc(gncode);
		return lsMaps;
	}
	@Override
	public String  getxh() throws Exception {
		String  xh = dmTreeDao.getxh();
		return xh;
	}
	/**
	 * 加载下拉
	 */
	
	public List<TreeJson> loadSelect(List<Map<String, String>> dataMapList,String idKey,String TextKey){
		
		List<TreeJson> sleectList = new ArrayList<>();
		for (Map<String, String> map : dataMapList) {
			//顶级目录
			if(map.get(idKey) != null){
				TreeJson tree = new TreeJson();
				tree.setId(map.get(idKey));
				tree.setText(map.get(TextKey));
				sleectList.add(tree);
			}
		}
		return sleectList;
	}
	
	

	/**
	 * 树结构加载方法
	 * @param dataMapList 表数据Map类型
	 * @param pidValue	  父级节点值
	 * @param pidKey	  父级ID字段名
	 * @param idKey		ID字段名
	 * @param TextKey	名称字段名
	 * @return
	 */
	public List<TreeJson> loadTree(List<Map<String, String>> dataMapList,String pidValue,String pidKey,String idKey,String TextKey){
	
		List<TreeJson> treeList = new ArrayList<>();
		for (Map<String, String> map : dataMapList) {
			//顶级目录
			if(map.get(pidKey) == null || pidValue.equals(map.get(pidKey))){
				TreeJson tree = new TreeJson();
				tree.setPid(map.get(pidKey));
				tree.setId(map.get(idKey));
				tree.setText(map.get(TextKey));
				tree.setState("closed");

				//加载子集树
				loadChildren(tree, dataMapList,tree.getId(), pidKey, idKey, TextKey);
			
				treeList.add(tree);
			}
		}
		
		return treeList;
	}
	
	/**
	 * 加载子节点
	 * @param parentTree   树对象
	 * @param dataMapList  表数据 
	 * @param pidValue		父节点ID值
	 * @param pidKey		父节点对应字段名
	 * @param idKey			该节点ID字段名
	 * @param TextKey		该节点文本字段名
	 */
	public void loadChildren(TreeJson parentTree,List<Map<String, String>> dataMapList,String pidValue,String pidKey,String idKey,String TextKey){
		List<TreeJson> treeList = new ArrayList<>();
		for (Map<String, String> map : dataMapList) {
			if(pidValue.equals(map.get(pidKey))){
				TreeJson tree = new TreeJson();
				tree.setPid(pidValue);
				tree.setId(map.get(idKey));
				tree.setText(map.get(TextKey));
				// 判断是否还有子集
				loadChildren(tree, dataMapList, tree.getId(),pidKey,  idKey, TextKey);
				treeList.add(tree);
			}
		}
		if(treeList.size() > 0){
			parentTree.setChildren(treeList);
		}
		
		return ;
	}
}
