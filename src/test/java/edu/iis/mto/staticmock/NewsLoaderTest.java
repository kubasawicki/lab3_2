package edu.iis.mto.staticmock;

import static edu.iis.mto.staticmock.SubsciptionType.*;
import edu.iis.mto.staticmock.reader.NewsReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author 204641
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})
public class NewsLoaderTest {
    
    IncomingNews incomingNews = new IncomingNews();
    ConfigurationLoader configurationLoaderMock;
    PublishableNews publishableNewsMock;
    NewsReader newsReaderMock;
    NewsLoader newsLoader;
    PublishableNews publishableNews;  
    
    List<String> newsContent;
    List<String> newsList;
    
    
    public NewsLoaderTest() {
    }
    @Before
    public void setUp() {
        mockStatic(ConfigurationLoader.class);
        configurationLoaderMock = mock(ConfigurationLoader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoaderMock);
        when(configurationLoaderMock.loadConfiguration()).thenReturn(new Configuration());
        
        newsReaderMock = mock(NewsReader.class);
        when(newsReaderMock.read()).thenReturn(incomingNews);
        mockStatic(NewsReaderFactory.class);
        when(NewsReaderFactory.getReader((String) Mockito.any())).thenReturn(newsReaderMock);
        newsLoader = new NewsLoader();
                
        mockStatic(PublishableNews.class);
        publishableNewsMock = mock(PublishableNews.class);
        
        incomingNews.add(new IncomingInfo("a", A));
        incomingNews.add(new IncomingInfo("none", NONE));
        publishableNews = newsLoader.loadNews();
        newsList = new ArrayList<>();
    }
    
    @Test
    public void testLoadNewsAddingIncomingInfoWhichIsNoneGetsAddedToPublicInfo(){
        
        newsContent = publishableNews.getPublicContent();
        newsList.add("none");
        assertThat(newsContent, is(newsList));

    }
    
    /**
     * Test of loadNews method, of class NewsLoader.
     */
    /*
    @Test
    public void testLoadNewsCallShouldCallLoadConfigurationMethodOfConfigurationloader() {
        
    }
    
    @Test
    public void testLoadNewsCallShouldCallGetReaderMethodOfNewsReaderFactory() {    
    }
    
    @Test
    public void testLoadNewsCallShouldCallReadMethodOfReader() {
    }
    
    @Test
    public void testLoadNewsCallShouldCallPrepareForPublicMethodOfNewsLoader() {
    }
    */
}
