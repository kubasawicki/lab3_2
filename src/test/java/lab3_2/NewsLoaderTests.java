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

import java.lang.reflect.Field;
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
	private Configuration config;
	private NewsLoader newsLoader;
	private NewsReader newsReader;
	private IncomingInfo AInfo; 
	private IncomingInfo BInfo;
	private IncomingInfo CInfo;
	private IncomingInfo publicInfo; 
	private IncomingNews incomingNews;
	private PublishableNews publishableNews;
	private Field field;
	
	@Before
	public void setUp() {
		
		newsLoader = new NewsLoader();
		newsReader = PowerMockito.mock(FileNewsReader.class);
		incomingNews = new IncomingNews();
		AInfo = new IncomingInfo("A", SubsciptionType.A);
		BInfo = new IncomingInfo("B", SubsciptionType.B);
		CInfo = new IncomingInfo("C", SubsciptionType.C);
		publicInfo = new IncomingInfo("Public", SubsciptionType.NONE);		
		publishableNews = new PublishableNews();		
		configLoader = PowerMockito.mock(ConfigurationLoader.class);
        config = new Configuration();

        PowerMockito.mockStatic(NewsReaderFactory.class);
        PowerMockito.mockStatic(ConfigurationLoader.class);

        PowerMockito.when(ConfigurationLoader.getInstance()).thenReturn(configLoader);
        PowerMockito.when(configLoader.loadConfiguration()).thenReturn(config);
        PowerMockito.when(NewsReaderFactory.getReader(config.getReaderType())).thenReturn(newsReader);
        PowerMockito.when(newsReader.read()).thenReturn(incomingNews);
        
        incomingNews.add(AInfo);	
        incomingNews.add(BInfo);
        incomingNews.add(CInfo);
        incomingNews.add(publicInfo);
		
	}
	
	@Test
    public void incomingNewsShouldContainCorrectNumberOfNews(){

        IncomingNews resultIncomingNews = newsReader.read();
        Assert.assertThat(resultIncomingNews.elems().size(), is(4));
    }
	
	@Test
	public void publicNewsTest() {

		try {
			field = PublishableNews.class.getDeclaredField("publicContent");
	        field.setAccessible(true);
	        List<String> result = (List<String>) field.get(newsLoader.loadNews());
	        Assert.assertThat(result.size(), is(1));
		} catch(Exception e) {
            System.out.println("No such field exception");
        }		
	}
	
	
}
