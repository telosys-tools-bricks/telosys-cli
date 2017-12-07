/**
 *  Copyright (C) 2015-2017  Telosys project org. ( http://www.telosys.org/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.telosys.tools.cli;


public class Banner {
	
	public String getBanner() {
		return banner2();
	}
	protected String banner1() {
		StringBuilder buf = new StringBuilder();
		buf.append("\n=======================================" );
		buf.append("\n* *" );
		buf.append("\n* TELOSYS TOOLS *" );
		buf.append("\n* *" );
		buf.append("\n=======================================");
		buf.append("\nVersion:" + this.getVersion());
		return buf.toString();
	}
	protected String banner2() {
		StringBuilder buf = new StringBuilder();
		buf.append("" );
		buf.append("\n  _       _                        "  ) ;
		buf.append("\n | |_ ___| | ___  ___ _   _ ___    " ) ;
		buf.append("\n | __/ _ \\ |/ _ \\/ __| | | / __| " ) ;
		buf.append("\n | ||  __/ | (_) \\__ \\ |_| \\__ \\ " ) ;
		buf.append("\n  \\__\\___|_|\\___/|___/\\__, |___/ " ) ;
		buf.append("\n                      |___/                             " ) ;
		buf.append(" \n version  " + this.getVersion());
		return buf.toString();
	}
	protected String banner3() {
		StringBuilder buf = new StringBuilder();
		buf.append(" " );
		
		buf.append("\n _____     ______                                _____            ______       "  ) ;
		buf.append("\n __  /________  /_________________  _________    __  /_______________  /_______"  ) ;
		buf.append("\n _  __/  _ \\_  /_  __ \\_  ___/_  / / /_  ___/    _  __/  __ \\  __ \\_  /__  ___/"  ) ;
		buf.append("\n / /_ /  __/  / / /_/ /(__  )_  /_/ /_(__  )     / /_ / /_/ / /_/ /  / _(__  ) "  ) ;
		buf.append("\n \\__/ \\___//_/  \\____//____/ _\\__, / /____/      \\__/ \\____/\\____//_/  /____/  "  ) ;
		buf.append("\n                             /____/                                            "  ) ;
		buf.append("\n " + this.getVersion());
		return buf.toString();
	}

	protected String bannerSlant() {
		StringBuilder sb = new StringBuilder();
		sb.append(" " );
		
		sb.append("\n    ______     __                     ");
		sb.append("\n   /_  __/__  / /___  _______  _______");
		sb.append("\n    / / / _ \\/ / __ \\/ ___/ / / / ___/");
		sb.append("\n   / / /  __/ / /_/ (__  ) /_/ (__  ) ");
		sb.append("\n  /_/  \\___/_/\\____/____/\\__, /____/  ");
		sb.append("\n                        /____/  " + this.getVersion() );
		return sb.toString();
	}
/* 

Big
  _______   _                     
 |__   __| | |                    
    | | ___| | ___  ___ _   _ ___ 
    | |/ _ \ |/ _ \/ __| | | / __|
    | |  __/ | (_) \__ \ |_| \__ \
    |_|\___|_|\___/|___/\__, |___/
                         __/ |    
                        |___/     

Doom 
 _____    _                     
|_   _|  | |                    
  | | ___| | ___  ___ _   _ ___ 
  | |/ _ \ |/ _ \/ __| | | / __|
  | |  __/ | (_) \__ \ |_| \__ \
  \_/\___|_|\___/|___/\__, |___/
                       __/ |    
                      |___/      
 
 Ogre
  _____     _                     
/__   \___| | ___  ___ _   _ ___ 
  / /\/ _ \ |/ _ \/ __| | | / __|
 / / |  __/ | (_) \__ \ |_| \__ \
 \/   \___|_|\___/|___/\__, |___/
                       |___/     
 Slant
  ______     __                     
 /_  __/__  / /___  _______  _______
  / / / _ \/ / __ \/ ___/ / / / ___/
 / / /  __/ / /_/ (__  ) /_/ (__  ) 
/_/  \___/_/\____/____/\__, /____/  
                      /____/        

Small
  _____    _                
 |_   _|__| |___ ____  _ ___
   | |/ -_) / _ (_-< || (_-<
   |_|\___|_\___/__/\_, /__/
                    |__/    

Standard
  _____    _                     
 |_   _|__| | ___  ___ _   _ ___ 
   | |/ _ \ |/ _ \/ __| | | / __|
   | |  __/ | (_) \__ \ |_| \__ \
   |_|\___|_|\___/|___/\__, |___/
                       |___/     

 */
	
	public String getVersion() {
		return Version.TELOSYS_TOOLS_CLI_VERSION ;
	}

}