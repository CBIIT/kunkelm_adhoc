/*
 * Copyright 2009-2015 PrimeTek.
 *
 * Licensed under PrimeFaces Commercial License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.primefaces.org/elite/license.xhtml
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.component.media.player;

import java.util.HashMap;
import java.util.Map;

public class MediaPlayerFactory {

	private static Map<String,MediaPlayer> players;
	
	/**
	 * @return Provides all players configured by this factory
	 */
	public static Map<String,MediaPlayer> getPlayers() {
		if(players == null) {
			players = new HashMap<String, MediaPlayer>();
			players.put(MediaPlayer.QUICKTIME, new QuickTimePlayer());
			players.put(MediaPlayer.FLASH, new FlashPlayer());
			players.put(MediaPlayer.WINDOWS, new WindowsPlayer());
			players.put(MediaPlayer.REAL, new RealPlayer());
            players.put(MediaPlayer.PDF, new PDFPlayer());
		}
		
		return players;
	}
	
	/**
	 * @return the specific player
	 */
	public static MediaPlayer getPlayer(String type) {
		if(type == null)
			throw new IllegalArgumentException("A media player type must be provided");
		
		MediaPlayer player = getPlayers().get(type);
		
		if(player != null)
			return player;
		else
			throw new IllegalArgumentException(type + " is not a valid media player type");
	}
}