package edu.iis.mto.staticmock;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PublishableNews that = (PublishableNews) o;

		return new EqualsBuilder()
				.append(publicContent, that.publicContent)
				.append(subscribentContent, that.subscribentContent)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(publicContent)
				.append(subscribentContent)
				.toHashCode();
	}
}
