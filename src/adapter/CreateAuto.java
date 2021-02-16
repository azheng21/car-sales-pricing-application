package adapter;

public interface CreateAuto {
	public String buildAuto(String fileName, String fileType);

	public boolean printAuto(String automobileKey);

	public boolean serialize(String automobileKey, String fileName);

	public String deserialize(String fileName);
}