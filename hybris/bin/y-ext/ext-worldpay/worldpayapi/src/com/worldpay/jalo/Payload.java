package com.worldpay.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import org.apache.log4j.Logger;

public class Payload extends GeneratedPayload
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger( Payload.class.getName() );

	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		// business code placed here will be executed before the item is created
		// then create the item
		return super.createItem( ctx, type, allAttributes );
	}

}
