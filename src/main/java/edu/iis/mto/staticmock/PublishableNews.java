package edu.iis.mto.staticmock;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PublishableNews that = (PublishableNews) o;
		return Objects.equals(publicContent, that.publicContent) &&
				Objects.equals(subscribentContent, that.subscribentContent);
	}

	@Override
	public int hashCode() {

		return Objects.hash(publicContent, subscribentContent);
	}
}
