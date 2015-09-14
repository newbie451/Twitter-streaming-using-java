import com.mongodb.MongoClient;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import com.mongodb.*;

import java.util.Arrays;

public class SimpleStream {
	public static void main(String[] args) {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey("YOUR_KEY_HERE");
		cb.setOAuthConsumerSecret("YOUR_KEY_HERE");
		cb.setOAuthAccessToken("YOUR_KEY_HERE");
		cb.setOAuthAccessTokenSecret("YOUR_KEY_HERE");

		TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

		StatusListener listener = new StatusListener() {

			@Override
			public void onException(Exception arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrubGeo(long arg0, long arg1) {
				// TODO Auto-generated method stub

			}
			int count=0;
			@Override
			public void onStatus(Status status) {
				User user = status.getUser();
				String username = status.getUser().getScreenName();
				System.out.println(username);
				String profileLocation = user.getLocation();
				System.out.println(profileLocation);
				long tweetId = status.getId(); 
				System.out.println(tweetId);
				String content = status.getText();
				System.out.println(content +"\n");
				count++;
				if(count==201)			//Store 200 tweets
					System.exit(0); ;

				try{   
					// Connect to mongodb server
					MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
					// Now connect to your databases
					DB db = mongoClient.getDB( "Twitter1" );       
					DBCollection coll = db.getCollection("Tweets");
					BasicDBObject doc = new BasicDBObject();
					doc.put("tweet",content);
					coll.insert(doc);

				}catch(Exception e){
					System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				}
			}

			@Override
			public void onTrackLimitationNotice(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub

			}

		};
		FilterQuery fq = new FilterQuery();

		fq.language(new String[]{"en"});
		double [][]location ={{-122.75,36.8},{-121.75,37.8}};
		fq.locations(location);


		twitterStream.addListener(listener);
		twitterStream.filter(fq);  
	}
}
