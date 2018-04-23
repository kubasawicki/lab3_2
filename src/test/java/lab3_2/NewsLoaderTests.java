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
	private String readerType;
	private IncomingInfo AInfo; 
	private IncomingInfo BInfo;
	private IncomingInfo CInfo;
	private IncomingInfo publicInfo; 
	private IncomingNews incomingNews;
	private PublishableNews publishableNews;
	
	@Before
	public void setUp() {
		
		newsLoader = new NewsLoader();
		readerType = "ReaderType";
		AInfo = new IncomingInfo("A", SubsciptionType.A);
		BInfo = new IncomingInfo("B", SubsciptionType.B);
		CInfo = new IncomingInfo("C", SubsciptionType.C);
		publicInfo = new IncomingInfo("Public", SubsciptionType.NONE);
		incomingNews = new IncomingNews();	
		
		configLoader = PowerMockito.mock(ConfigurationLoader.class);
		when(configLoader.loadConfiguration()).thenReturn(new Configuration());
		
		newsReader = PowerMockito.mock(FileNewsReader.class);
		when(newsReader.read()).thenReturn(incomingNews);
		
		mockStatic(NewsReaderFactory.class);
		when(NewsReaderFactory.getReader(Mockito.anyString())).thenReturn(newsReader);			
		
		mockStatic(NewsReaderFactory.class);		
		when(NewsReaderFactory.getReader(readerType)).thenReturn(newsReader);
		
		newsLoader = new NewsLoader();
	}
}
