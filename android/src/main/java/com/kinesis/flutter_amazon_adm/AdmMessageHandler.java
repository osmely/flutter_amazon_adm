/*
 * [AdmMessageHandler.java]
 *
 * (c) 2019, Amazon.com, Inc. or its affiliates. All Rights Reserved.
 */

package com.kinesis.flutter_amazon_adm;

import android.os.Handler;
import android.os.Looper;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.amazon.device.messaging.ADMConstants;
import com.amazon.device.messaging.ADMMessageHandlerBase;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class AdmMessageHandler extends ADMMessageHandlerBase
{

    /**
     * Class constructor.
     */
    public AdmMessageHandler()
    {
        super(AdmMessageHandler.class.getName());
    }
	
    /**
     * Class constructor, including the className argument.
     * 
     * @param className The name of the class.
     */
    public AdmMessageHandler(final String className) 
    {
        super(className);
    }

    /** {@inheritDoc} */
    @Override
    protected void onMessage(final Intent intent) 
    {
    

    }


    /** {@inheritDoc} */
    @Override
    protected void onRegistrationError(final String string)
    {
        
    }

    public static void sendRegistrationIdToDart(String registrationId) {
        
    }

    /** {@inheritDoc} */
    @Override
    protected void onRegistered(final String registrationId) 
    {
        
    }

    /** {@inheritDoc} */
    @Override
    protected void onUnregistered(final String registrationId) 
    {
     
    }

    @Override
    protected void onSubscribe(final String topic) {
        
    }

    @Override
    protected void onSubscribeError(final String topic, final String errorId) {
        
    }

    @Override
    protected void onUnsubscribe(final String topic) {
        
    }

    @Override
    protected void onUnsubscribeError(final String topic, final String errorId) {
        
    }
}
