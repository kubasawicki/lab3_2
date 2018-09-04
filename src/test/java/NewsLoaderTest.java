import edu.iis.mto.staticmock.*;
import edu.iis.mto.staticmock.reader.NewsReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.VerificationModeFactory;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})
public class NewsLoaderTest {
    private IncomingNews incomingNews = new IncomingNews();
    private NewsLoader newsLoader;
    private ConfigurationLoader configurationLoaderMock;

    @Before
    public void setUp() throws Exception {
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
    public void shouldLoadNewsDoOneTime() {
        newsLoader.loadNews();
        verify(configurationLoaderMock, VerificationModeFactory.times(1)).loadConfiguration();
    }

    @Test
    public void shouldNewsLoaderReturnTheSameContext() {
        IncomingInfo incomingInfoA = new IncomingInfo("A", SubsciptionType.A);
        IncomingInfo incomingInfoB = new IncomingInfo("B", SubsciptionType.B);
        IncomingInfo incomingInfoC = new IncomingInfo("C", SubsciptionType.C);
        IncomingInfo incomingInfoNone = new IncomingInfo("None", SubsciptionType.NONE);

        incomingNews.add(incomingInfoA);
        incomingNews.add(incomingInfoB);
        incomingNews.add(incomingInfoC);
        incomingNews.add(incomingInfoNone);

        PublishableNews publishableNewsDummy = new PublishableNews();
        publishableNewsDummy.addForSubscription("A", SubsciptionType.A);
        publishableNewsDummy.addForSubscription("B", SubsciptionType.B);
        publishableNewsDummy.addForSubscription("C", SubsciptionType.C);
        publishableNewsDummy.addPublicInfo("None");

        Assert.assertTrue(publishableNewsDummy.equals(newsLoader.loadNews()));
    }
}