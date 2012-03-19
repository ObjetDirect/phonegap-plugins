/**
 * Base of the Android version for Twitter 
 * @see https://github.com/phonegap/phonegap-plugins/tree/master/iPhone/Twitter
 */
if(!PhoneGap.hasResource("twitter")) {
	PhoneGap.addResource("twitter");
	
	/**
	 * Constructor
	 */
	function Twitter() {
	};

    /**
     * Check if the serrvice is available
     */
	Twitter.prototype.isTwitterAvailable = function(success, failure) {
	    PhoneGap.exec(success, failure, "Twitter", "isTwitterAvailable", []);
	};
	
	/**
	 * Try to open Twitter for sending the message
	 */
	Twitter.prototype.composeTweet = function(success, failure, tweetText, options) {
	    PhoneGap.exec(success, failure, "Twitter", "composeTweet", [tweetText]);
	};
	
	// Declare the plugin into Phonegap
	PhoneGap.addConstructor(function() {
	    PhoneGap.addPlugin("twitter", new Twitter());
	});
}
