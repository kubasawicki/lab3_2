package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.reader.FileNewsReader;
import edu.iis.mto.staticmock.reader.NewsReader;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})
public class NewsLoaderTest {

    private IncomingNews incomingNews = new IncomingNews();
    private NewsLoader newsLoader;
    private ConfigurationLoader configurationLoaderMock;

    @Before
    public void setUp() {
        mockStatic(ConfigurationLoader.class);
        configurationLoaderMock = PowerMockito.mock(ConfigurationLoader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoaderMock);
        when(configurationLoaderMock.loadConfiguration()).thenReturn(new Configuration());

        NewsReader newsReaderMock = mock(NewsReader.class);
        when(newsReaderMock.read()).thenReturn(incomingNews);

        mockStatic(NewsReaderFactory.class);
        NewsReaderFactory newsReaderFactory = mock(NewsReaderFactory.class);
        when(newsReaderFactory.getReader(null)).thenReturn(newsReaderMock);

        newsLoader = new NewsLoader();
    }

    @Test
    public void callLoadConfigurationInLoadNewsMethod() {

        newsLoader.loadNews();
        verify(configurationLoaderMock, times(1)).loadConfiguration();
    }

    @Test
    public void checkIfNewsLoaderLoadOnlyPublicNews() {
        IncomingInfo incomingInfo1 = new IncomingInfo("1", SubsciptionType.A);
        IncomingInfo incomingInfo2 = new IncomingInfo("2", SubsciptionType.B);
        IncomingInfo incomingInfo3 = new IncomingInfo("3", SubsciptionType.C);
        IncomingInfo incomingInfoNone = new IncomingInfo("None", SubsciptionType.NONE);

        incomingNews.add(incomingInfo1);
        incomingNews.add(incomingInfo2);
        incomingNews.add(incomingInfo3);
        incomingNews.add(incomingInfoNone);

        PublishableNews publishableNewsDummy = new PublishableNews();
        publishableNewsDummy.addPublicInfo("None");
        publishableNewsDummy.addForSubscription("1", SubsciptionType.A);
        publishableNewsDummy.addForSubscription("2", SubsciptionType.B);
        publishableNewsDummy.addForSubscription("3", SubsciptionType.C);

        Assert.assertTrue(publishableNewsDummy.equals(newsLoader.loadNews()));
    }

}