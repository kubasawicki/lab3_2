package lab3_2;

import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import edu.iis.mto.staticmock.*;
import edu.iis.mto.staticmock.reader.FileNewsReader;
import edu.iis.mto.staticmock.reader.NewsReader;

import static org.powermock.api.mockito.PowerMockito.*;

import java.util.List;

import org.codehaus.jackson.map.DeserializerFactory.Config;

import static org.hamcrest.MatcherAssert.*;
import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.times;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@RunWith(PowerMockRunner.class)
@PrepareForTest( {ConfigurationLoader.class, NewsReaderFactory.class, PublishableNews.class} )
public class NewsLoaderTests {

	private ConfigurationLoader configLoader;
	private NewsLoader newsLoader;
	private NewsReader newsReader;
	private IncomingInfo AInfo; 
	private IncomingInfo BInfo;
	private IncomingInfo CInfo;
	private IncomingInfo publicInfo; 
	private IncomingNews incomingNews;
	private PublishableNews publishableNews;
	private String readerType;
	
	@Before
	public void setUp() {
		
		newsLoader = new NewsLoader();
		AInfo = new IncomingInfo("A", SubsciptionType.A);
		BInfo = new IncomingInfo("B", SubsciptionType.B);
		CInfo = new IncomingInfo("C", SubsciptionType.C);
		publicInfo = new IncomingInfo("Public", SubsciptionType.NONE);		
		publishableNews = new PublishableNews();
		readerType = new String("ReaderType");
		
		mockStatic(ConfigurationLoader.class);
		ConfigurationLoader mockConfigurationLoader = mock(ConfigurationLoader.class);
		when(ConfigurationLoader.getInstance()).thenReturn(mockConfigurationLoader);
		Configuration configuration = new Configuration();
		Whitebox.setInternalState(configuration, "readerType", readerType);
		when(mockConfigurationLoader.loadConfiguration()).thenReturn(configuration);
		
		incomingNews = new IncomingNews();	
		incomingNews.add(AInfo);
		incomingNews.add(BInfo);
		incomingNews.add(CInfo);
		incomingNews.add(publicInfo);
		
		NewsReader newsReader = new NewsReader() {
			
			@Override
			public IncomingNews read() {
				return incomingNews;				
			}
		};
		
		mockStatic(NewsReaderFactory.class);
		when(NewsReaderFactory.getReader(readerType)).thenReturn(newsReader);
		
	}
	
	@Test
	public void publicNewsTest() {
		
		publishableNews = newsLoader.loadNews();
		List<String> result = (List<String>) Whitebox.getInternalState(publishableNews, "publicContent");
		Assert.assertThat(result.size(), is(1));
	}
}
