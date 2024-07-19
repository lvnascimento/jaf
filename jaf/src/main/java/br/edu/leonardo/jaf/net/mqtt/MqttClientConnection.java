package br.edu.leonardo.jaf.net.mqtt;

import br.edu.leonardo.jaf.net.NetworkException;
import java.net.URI;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import javax.net.SocketFactory;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * A manager for MQTT client connection to a server.
 * 
 * @author Leonardo Vianna do Nascimento (lvianna@gmail.com)
 */
public class MqttClientConnection {
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   C O N S T R U C T O R S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This constructor builds a new MqttClientConnection that uses an internal generated client
     * identification.
     * 
     * @throws NetworkException If an error occurs during object creation. 
     */
    public MqttClientConnection() throws NetworkException {
        this(MqttClient.generateClientId());
    }

    /**
     * This constructor builds a new MqttClientConnection that uses the given client id.
     * 
     * @param clientId The id of this client.
     * @throws NetworkException If an error occurs during object creation.
     */
    public MqttClientConnection(String clientId) throws NetworkException {
        try {
            // Create the internal Paho MqttClient object. The serverURI parameter is typically used
            // with the the clientId parameter to form a key. The key is used to store and reference 
            // messages while they are being delivered. Hence the serverURI specified on the constructor 
            // must still be specified even if a list of servers is specified on an MqttConnectOptions 
            // object (the option that is used in this implementation). Thus, the line below uses a 
            // hypotethic URI to localhost and a port number based on the current time in millis.
            client = new MqttClient("tcp://localhost:"+System.currentTimeMillis(), clientId);
            
            // Set the callback object that will receive notifications from the client.
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable thrwbl) {
                    // When a connection to the server is lost, the method onConnectionLost of all 
                    // listeners is called to deal with this situation.
                    for(MqttClientListener listener : listeners) {
                        listener.onConnectionLost(thrwbl);
                    }
                }

                @Override
                public void messageArrived(String topic, MqttMessage mm) throws Exception {
                    // When a message arrives this object notifies all listeners and gives them the
                    // received payload.
                    byte[] payload = mm.getPayload();
                    for(MqttClientListener listener : listeners) {
                        listener.onMessageReceived(payload);
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken imdt) {
                }
            });
        } catch (MqttException ex) {
            throw new NetworkException(ex);
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P U B L I C   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * This method adds a new listener to this object. A listener is used to receive notifications
     * when a connection is lost or when a message arrives.
     * 
     * @param listener The new listeners.
     */
    public void addListener(MqttClientListener listener) {
        listeners.add(listener);
    }
    
    /**
     * This method removes a listener from this object.
     * 
     * @param listener The listener to be removed. 
     */
    public void removeListener(MqttClientListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * This method connects to the given server URI without an username and a password. This is
     * a blocking method that returns once connect completes. It is recommended to add listeners
     * prior to connecting in order that messages destined for the client can be accepted as 
     * soon as the client is connected.
     * 
     * @param serverURI The server URI.
     * @throws NetworkException If an error occurs during the connection attempt.
     */
    public void connect(URI serverURI) throws NetworkException {
        connect(new URI[]{serverURI});
    }
    
    /**
     * This method tries to connect to the given servers without an username and a password. When 
     * an attempt to connect is initiated the client will start with the first serverURI in the list 
     * and work through the list until a connection is established with a server. If a connection 
     * cannot be made to any of the servers then the connect attempt fails. This is a blocking 
     * method that returns once connect completes. It is recommended to add listeners prior to 
     * connecting in order that messages destined for the client can be accepted as soon as the 
     * client is connected.
     * 
     * @param serverURIs The URIs of the servers.
     * @throws NetworkException If an error occurs during the connection attempt.
     */
    public void connect(URI[] serverURIs) throws NetworkException {
        connect(serverURIs, null, null);
    }
    
    /**
     * This method tries to connect to the given server using the given user name and password.
     * This is a blocking method that returns once connect completes. It is recommended to add 
     * listeners prior to connecting in order that messages destined for the client can be 
     * accepted as soon as the client is connected.
     * 
     * @param serverURI The server URI.
     * @param username The user name to use for the connection.
     * @param password The password to use for the connection.
     * @throws NetworkException If an error occurs during the connection attempt.
     */
    public void connect(URI serverURI, String username, char[] password) throws NetworkException {
        connect(new URI[]{serverURI}, username, password);
    }
    
    /**
     * This method tries to connect to the given servers using the given user name and password. When 
     * an attempt to connect is initiated the client will start with the first serverURI in the list 
     * and work through the list until a connection is established with a server. If a connection 
     * cannot be made to any of the servers then the connect attempt fails. This is a blocking 
     * method that returns once connect completes. It is recommended to add listeners prior to 
     * connecting in order that messages destined for the client can be accepted as soon as the 
     * client is connected.
     * 
     * @param serverURIs The URIs of the servers.
     * @param username The user name to use for the connection.
     * @param password The password to use for the connection.
     * @throws NetworkException If an error occurs during the connection attempt.
     */
    public void connect(URI[] serverURIs, String username, char[] password) throws NetworkException {
        String[] strURIs = new String[serverURIs.length];
        for(int i = 0; i < strURIs.length; i++) {
            strURIs[i] = serverURIs[i].toString();
        }
        options.setServerURIs(strURIs);
        options.setUserName(username);
        options.setPassword(password);
        connect();
    }
    
    /**
     * This method subscribes to a topic, which may include wildcards using the 
     * given QoS. The "topic" string used when subscribing may contain special 
     * characters, which allow you to subscribe to multiple topics at once. The 
     * allowed characters can be checked at <a href=https://eclipse.dev/paho/files/javadoc/org/eclipse/paho/client/mqttv3/MqttClient.html#subscribe-java.lang.String:A-int:A-">this link</a>.
     * 
     * @param topic The topic to subscribe to, which can include wildcards.
     * @param qos The maximum quality of service at which to subscribe. Messages published at a 
     *            lower quality of service will be received at the published QoS. Messages published 
     *            at a higher quality of service will be received using the QoS specified on the 
     *            subscribe.
     * @throws NetworkException If there was an error registering the subscription.
     */
    public void subscribe(String topic, int qos) throws NetworkException {
        try {
            client.subscribe(topic, qos);
        } catch (MqttException ex) {
            throw new NetworkException(ex);
        }
    }
    
    /**
     * This method subscribes to a topic, which may include wildcards using the 
     * given QoS. The "topic" string used when subscribing may contain special 
     * characters, which allow you to subscribe to multiple topics at once. The 
     * allowed characters can be checked at <a href=https://eclipse.dev/paho/files/javadoc/org/eclipse/paho/client/mqttv3/MqttClient.html#subscribe-java.lang.String:A-int:A-">this link</a>. 
     * 
     * @param topics One or more topics to subscribe to, which can include wildcards.
     * @param qos The maximum quality of service to subscribe each topic at. Messages published at a lower 
     *            quality of service will be received at the published QoS. Messages published at a higher 
     *            quality of service will be received using the QoS specified on the subscribe.
     * @throws NetworkException If there was an error registering the subscription.
     */
    public void subscribe(String[] topics, int[] qos) throws NetworkException {
        try {
            client.subscribe(topics, qos);
        } catch (MqttException ex) {
            throw new NetworkException(ex);
        }
    }
    
    /**
     * This method connects to a server specified by the given URI and after subscribes
     * to the given topic using the given QoS. See the methods connect(URI) and 
     * subscribe(String, int) for more details.
     * 
     * @param serverURI The server URI.
     * @param topic The topic to subscribe to, which can include wildcards.
     * @param qos The maximum quality of service at which to subscribe. Messages published at a 
     *            lower quality of service will be received at the published QoS. Messages published 
     *            at a higher quality of service will be received using the QoS specified on the 
     *            subscribe.
     * @throws NetworkException If an error occurs during the connection attempt or registering 
     *                          the subscription.
     */
    public void connectAndSubscribe(URI serverURI, String topic, int qos) throws NetworkException {
        connect(serverURI);
        subscribe(topic, qos);
    }
    
    /**
     * This method attempts to connect to the given servers and after subscribes
     * to the given topic using the given QoS. See the methods connect(URI[]) and 
     * subscribe(String, int) for more details.
     * 
     * @param serverURIs The URIs of the servers.
     * @param topic The topic to subscribe to, which can include wildcards.
     * @param qos The maximum quality of service at which to subscribe. Messages published at a 
     *            lower quality of service will be received at the published QoS. Messages published 
     *            at a higher quality of service will be received using the QoS specified on the 
     *            subscribe.
     * @throws NetworkException If an error occurs during the connection attempt or registering 
     *                          the subscription.
     */
    public void connectAndSubscribe(URI[] serverURIs, String topic, int qos) throws NetworkException {
        connect(serverURIs);
        subscribe(topic, qos);
    }
    
    /**
     * This method connects to a server specified by the given URI and after subscribes
     * to the given topics using the given QoS values. See the methods connect(URI) and 
     * subscribe(String[], int[]) for more details.
     * 
     * @param serverURI The server URI.
     * @param topics One or more topics to subscribe to, which can include wildcards.
     * @param qos The maximum quality of service to subscribe each topic at. Messages published at a lower 
     *            quality of service will be received at the published QoS. Messages published at a higher 
     *            quality of service will be received using the QoS specified on the subscribe.
     * @throws NetworkException If an error occurs during the connection attempt or registering 
     *                          the subscription.
     */
    public void connectAndSubscribe(URI serverURI, String[] topics, int[] qos) throws NetworkException {
        connect(serverURI);
        subscribe(topics, qos);
    }
    
    /**
     * This method attempts to connect to the given servers and after subscribes
     * to the given topics using the given QoS values. See the methods connect(URI[]) and 
     * subscribe(String[], int[]) for more details.
     * 
     * @param serverURIs The URIs of the servers.
     * @param topics One or more topics to subscribe to, which can include wildcards.
     * @param qos The maximum quality of service to subscribe each topic at. Messages published at a lower 
     *            quality of service will be received at the published QoS. Messages published at a higher 
     *            quality of service will be received using the QoS specified on the subscribe.
     * @throws NetworkException If an error occurs during the connection attempt or registering 
     *                          the subscription.
     */
    public void connectAndSubscribe(URI[] serverURIs, String[] topics, int[] qos) throws NetworkException {
        connect(serverURIs);
        subscribe(topics, qos);
    }
    
    /**
     * This method attempts to connect to the given server using the given username 
     * and password, and after subscribes to the given topic using the given QoS. See 
     * the methods connect(URI, String, char[]) and subscribe(String, int) for more details.
     * 
     * @param serverURI The server URI.
     * @param username The user name to use for the connection.
     * @param password The password to use for the connection.
     * @param topic The topic to subscribe to, which can include wildcards.
     * @param qos The maximum quality of service at which to subscribe. Messages published at a 
     *            lower quality of service will be received at the published QoS. Messages published 
     *            at a higher quality of service will be received using the QoS specified on the 
     *            subscribe.
     * @throws NetworkException If an error occurs during the connection attempt or registering 
     *                          the subscription.
     */
    public void connectAndSubscribe(URI serverURI, String username, char[] password, String topic, int qos) throws NetworkException {
        connect(serverURI, username, password);
        subscribe(topic, qos);
    }
    
    /**
     * This method attempts to connect to the given server using the given username 
     * and password, and after subscribes to the given topics using the given QoS values. See 
     * the methods connect(URI, String, char[]) and subscribe(String[], int[]) for more details.
     * 
     * @param serverURI The server URI.
     * @param username The user name to use for the connection.
     * @param password The password to use for the connection.
     * @param topics One or more topics to subscribe to, which can include wildcards.
     * @param qos The maximum quality of service to subscribe each topic at. Messages published at a lower 
     *            quality of service will be received at the published QoS. Messages published at a higher 
     *            quality of service will be received using the QoS specified on the subscribe.
     * @throws NetworkException If an error occurs during the connection attempt or registering 
     *                          the subscription.
     */
    public void connectAndSubscribe(URI serverURI, String username, char[] password, String[] topics, int[] qos) throws NetworkException {
        connect(serverURI, username, password);
        subscribe(topics, qos);
    }
    
    /**
     * This method attempts to connect to the given servers using the given username 
     * and password, and after subscribes to the given topic using the given QoS. See 
     * the methods connect(URI[], String, char[]) and subscribe(String, int) for more details.
     * 
     * @param serverURIs The URIs of the servers.
     * @param username The user name to use for the connection.
     * @param password The password to use for the connection.
     * @param topic The topic to subscribe to, which can include wildcards.
     * @param qos The maximum quality of service at which to subscribe. Messages published at a 
     *            lower quality of service will be received at the published QoS. Messages published 
     *            at a higher quality of service will be received using the QoS specified on the 
     *            subscribe.
     * @throws NetworkException If an error occurs during the connection attempt or registering 
     *                          the subscription.
     */
    public void connectAndSubscribe(URI[] serverURIs, String username, char[] password, String topic, int qos) throws NetworkException {
        connect(serverURIs, username, password);
        subscribe(topic, qos);
    }
    
    /**
     * This method attempts to connect to the given servers using the given username 
     * and password, and after subscribes to the given topics using the given QoS values. See 
     * the methods connect(URI[], String, char[]) and subscribe(String[], int[]) for more details.
     * 
     * @param serverURIs The URIs of the servers.
     * @param username The user name to use for the connection.
     * @param password The password to use for the connection.
     * @param topics One or more topics to subscribe to, which can include wildcards.
     * @param qos The maximum quality of service to subscribe each topic at. Messages published at a lower 
     *            quality of service will be received at the published QoS. Messages published at a higher 
     *            quality of service will be received using the QoS specified on the subscribe.
     * @throws NetworkException If an error occurs during the connection attempt or registering 
     *                          the subscription.
     */
    public void connectAndSubscribe(URI[] serverURIs, String username, char[] password, String[] topics, int[] qos) throws NetworkException {
        connect(serverURIs, username, password);
        subscribe(topics, qos);
    }
    
    /**
     * This method requests the server unsubscribe the client from a topic. This is a 
     * blocking method that returns once unsubscribe completes.
     * 
     * @param topicFilter The topic to unsubscribe from. It must match a topicFilter 
     *                    specified on the subscribe.
     * @throws NetworkException If there was an error unregistering the subscription.
     */
    public void unsubscribe(String topicFilter) throws NetworkException {
        try {
            client.unsubscribe(topicFilter);
        } catch (MqttException ex) {
            throw new NetworkException(ex);
        }
    }
    
    /**
     * This method requests the server unsubscribe the client from one or more topics. This is a 
     * blocking method that returns once unsubscribe completes.
     * 
     * @param topicFilters One or more topics to unsubscribe from. Each topicFilter must match one
     *                     specified on a subscribe
     * @throws NetworkException If there was an error unregistering the subscription.
     */
    public void unsubscribe(String[] topicFilters) throws NetworkException {
        try {
            client.unsubscribe(topicFilters);
        } catch (MqttException ex) {
            throw new NetworkException(ex);
        }
    }
    
    /**
     * This method disconnects from the server. This method must not be called from inside 
     * listener methods.
     * 
     * @throws NetworkException If there was an error unregistering the subscription.
     */
    public void disconnect() throws NetworkException {
        try {
            client.disconnect();
        } catch (MqttException ex) {
            throw new NetworkException(ex);
        }
    }
    
    /**
     * This method returns whether the client will automatically attempt to reconnect to
     * the server if the connection is lost.
     * 
     * @return The automatic reconnection flag.
     */
    public boolean isAutomaticReconnect() {
        return options.isAutomaticReconnect();
    }

    /**
     * This method sets whether the client will automatically attempt to reconnect to the
     * server if the connection is lost. 
     * 
     * @param automaticReconnect If set to false, the client will not attempt to
     * automatically reconnect to the server in the event that the connection is lost. If set
     * to true, in the event that the connection is lost, the client will attempt to reconnect
     * to the server. It will initially wait 1 second before it attempts to reconnect, for
     * every failed reconnect attempt, the delay will double until it is at 2 minutes at which
     * point the delay will stay at 2 minutes.
     */
    public void setAutomaticReconnect(boolean automaticReconnect) {
        options.setAutomaticReconnect(automaticReconnect);
    }

    /**
     * This method returns whether the client and server should remember state for the client
     * across reconnects.
     * 
     * @return The clean session flag.
     */
    public boolean isCleanSession() {
        return options.isCleanSession();
    }

    /**
     * This method sets whether the client and server should remember state across restarts and reconnects.
     * 
     * @param cleanSession If set to false both the client and server will maintain state across restarts of the client, 
     *                     the server and the connection. As state is maintained: Message delivery will be reliable 
     *                     meeting the specified QOS even if the client, server or connection are restarted. The server
     *                     will treat a subscription as durable. If set to true the client and server will not maintain
     *                     state across restarts of the client, the server or the connection. This means Message delivery 
     *                     to the specified QOS cannot be maintained if the client, server or connection are restarted. 
     *                     The server will treat a subscription as non-durable
     */
    public void setCleanSession(boolean cleanSession) {
        options.setCleanSession(cleanSession);
    }

    /**
     * This method returns the connection timeout configured for this connection.
     * 
     * @return The connection timeout value in seconds.
     */
    public Duration getConnectionTimeout() {
        return Duration.ofSeconds(options.getConnectionTimeout());
    }

    /**
     * The method sets the connection timeout value. This value defines the maximum time interval the client will
     * wait for the network connection to the MQTT server to be established. The default timeout is 30 seconds. A
     * value of 0 disables timeout processing meaning the client will wait until the network connection is made 
     * successfully or fails.
     * 
     * @param connectionTimeout The timeout value.
     */
    public void setConnectionTimeout(Duration connectionTimeout) {
        options.setConnectionTimeout((int) connectionTimeout.getSeconds());
    }

    /**
     * This method returns the "keep alive" interval, in seconds.
     * 
     * @return The keep alive interval value.
     */
    public Duration getKeepAliveInterval() {
        return Duration.ofSeconds(options.getKeepAliveInterval());
    }

    /**
     * This method sets the "keep alive" interval. This value defines the maximum time interval between
     * messages sent or received. It enables the client to detect if the server is no longer available, 
     * without having to wait for the TCP/IP timeout. The client will ensure that at least one message 
     * travels across the network within each keep alive period. In the absence of a data-related message
     * during the time period, the client sends a very small "ping" message, which the server will acknowledge.
     * A value of 0 disables keepalive processing in the client. The default value is 60 seconds.
     * 
     * @param keepAliveInterval The interval.
     */
    public void setKeepAliveInterval(Duration keepAliveInterval) {
        options.setKeepAliveInterval((int) keepAliveInterval.getSeconds());
    }

    /**
     * This method returns the MQTT version.
     * 
     * @return The MQTT version.
     */
    public int getMqttVersion() {
        return options.getMqttVersion();
    }

    /**
     * This method sets the MQTT version. The default action is to connect with version 3.1.1, and to fall back
     * to 3.1 if that fails. Version 3.1.1 or 3.1 can be selected specifically, with no fall back, by using the
     * MQTT_VERSION_3_1_1 or MQTT_VERSION_3_1 constants respectively.
     * 
     * @param mqttVersion The version of the MQTT protocol.
     */
    public void setMqttVersion(int mqttVersion) {
        options.setMqttVersion(mqttVersion);
    }
    
    /**
     * This method returns the socket factory that will be used when connecting, or null if one has not been set.
     * 
     * @return The socket factory.
     */
    public SocketFactory getSocketFactory() {
        return options.getSocketFactory();
    }
    
    /**
     * This method sets the SocketFactory to use. This allows an application to apply its own policies around the
     * creation of network sockets. If using an SSL connection, an SSLSocketFactory can be used to supply 
     * application-specific security settings.
     * 
     * @param factory The factory to use.
     */
    public void setSocketFactory(SocketFactory factory) {
        options.setSocketFactory(factory);
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   M E T H O D S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method connects to an MQTT server using the options defined in this object.
     * 
     * @throws NetworkException If an error occurred during the connection attempt.
     */
    private void connect() throws NetworkException {
        try {
            client.connect(options);
        } catch (MqttException ex) {
            throw new NetworkException(ex);
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // P R I V A T E   A T T R I B U T E S
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The object that stores the connection options.
     */
    private final MqttConnectOptions options = new MqttConnectOptions();
    
    /**
     * The PAHO MQTT client used by this object.
     */
    private final MqttClient client;
    
    /**
     * The listeners added to this object.
     */
    private final Set<MqttClientListener> listeners = new HashSet();
}
