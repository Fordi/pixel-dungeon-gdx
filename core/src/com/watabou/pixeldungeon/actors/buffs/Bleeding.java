/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.watabou.pixeldungeon.actors.buffs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.ResultDescriptions;
import com.watabou.pixeldungeon.effects.Splash;
import com.watabou.pixeldungeon.ui.BuffIndicator;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import static com.watabou.noosa.NoosaI18N.tr;

public class Bleeding extends Buff {
	private static final String TXT_NAME = "buff_bleeding_name";
	private static final String TXT_HERO_KILLED = "buff_bleeding_killed";

	protected int level;
	
	private static final String BUNDLE_KEY_LEVEL = "level";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put(BUNDLE_KEY_LEVEL, level);
		
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		level = bundle.getInt(BUNDLE_KEY_LEVEL);
	}
	
	public void set( int level ) {
		this.level = level;
	};
	
	@Override
	public int icon() {
		return BuffIndicator.BLEEDING;
	}
	
	@Override
	public String toString() {
		return tr(TXT_NAME);
	}
	
	@Override
	public boolean act() {
		if (target.isAlive()) {
			
			if ((level = Random.Int( level / 2, level )) > 0) {
				
				target.damage( level, this );
				if (target.sprite.visible) {
					Splash.at( target.sprite.center(), -PointF.PI / 2, PointF.PI / 6, 
							target.sprite.blood(), Math.min( 10 * level / target.HT, 10 ) );
				}
				
				if (target == Dungeon.hero && !target.isAlive()) {
					Dungeon.fail( tr(ResultDescriptions.BLEEDING, Dungeon.depth) );
					GLog.n(tr(TXT_HERO_KILLED));
				}
				
				spend( TICK );
			} else {
				detach();
			}
			
		} else {
			
			detach();
			
		}
		
		return true;
	}
}
