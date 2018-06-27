package edu.iis.mto.staticmock;

import java.util.ArrayList;
import java.util.List;

public class PublishableNews {

	public static PublishableNews create() {
		return new PublishableNews();
	}

	private final List<String> publicContent = new ArrayList<>();
	private final List<String> subscribentContent = new ArrayList<>();

	public void addPublicInfo(String content) {
		this.publicContent .add(content);
		
	}

	public void addForSubscription(String content, SubsciptionType subscriptionType) {
		// TODO Auto-generated method stub
		
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		PublishableNews that = (PublishableNews) o;

		if (publicContent != null ? !publicContent.equals(that.publicContent) : that.publicContent != null)
			return false;
		return subscribentContent != null ?
				subscribentContent.equals(that.subscribentContent) :
				that.subscribentContent == null;
	}

	@Override public int hashCode() {
		int result = publicContent != null ? publicContent.hashCode() : 0;
		result = 31 * result + (subscribentContent != null ? subscribentContent.hashCode() : 0);
		return result;
	}
}
