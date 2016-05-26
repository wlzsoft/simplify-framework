package com.meizu.simplify.utils;
/**
  * <p><b>Title:</b><i>超大实体对象，用于测试fastjson的stream的api的json转换速度</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月31日 下午3:24:51</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月31日 下午3:24:51</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class BigVo {
	private String auc = "234421082";
	private String item="3821";
	private String owner = "test"; 
	private String ownerRealm = "testaaa"; 
	private String bid = "84500";
	private String buyout="90000"; 
	private String quantity ="1";
	private String timeLeft = "VERY_LONG";
	private String rand="0";
	private String seed="385088896";
	public BigVo(){
		
	}
	public BigVo(int i) {
		this.auc+=i;
	}
	public String getAuc() {
		return auc;
	}
	public void setAuc(String auc) {
		this.auc = auc;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getOwnerRealm() {
		return ownerRealm;
	}
	public void setOwnerRealm(String ownerRealm) {
		this.ownerRealm = ownerRealm;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getBuyout() {
		return buyout;
	}
	public void setBuyout(String buyout) {
		this.buyout = buyout;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getTimeLeft() {
		return timeLeft;
	}
	public void setTimeLeft(String timeLeft) {
		this.timeLeft = timeLeft;
	}
	public String getRand() {
		return rand;
	}
	public void setRand(String rand) {
		this.rand = rand;
	}
	public String getSeed() {
		return seed;
	}
	public void setSeed(String seed) {
		this.seed = seed;
	}
	
}
