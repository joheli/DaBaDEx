package dabadex;

public interface DaBaDExParam {
	public DBData getdBData();

	public String getCronSchedule();
	
	public void reAttachParameters() throws Exception;
	
	public String getFollowUp();
}
