/**
 *  Copyright (C) 2015-2019 Telosys project org. ( http://www.telosys.org/ )
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
package org.telosys.tools.cli.commands.git;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.URIish;

public class GitRemote {

	private GitRemote() {
	}

	public static void setRemote(File gitWorkingTreeDir, String remoteName, String remoteUrl) throws IOException {
		try ( Repository repository = GitUtil.buildRepository(gitWorkingTreeDir) ) {
			StoredConfig storedConfig = repository.getConfig();
			// Set "remote" (overwrite existing value if any)
			storedConfig.setString("remote", remoteName, "url",   remoteUrl);
			storedConfig.setString("remote", remoteName, "fetch", "+refs/heads/*:refs/remotes/" + remoteName + "/*");
			storedConfig.save();
		} // close Repository
	}

	public static List<String> getRemotes(File gitWorkingTreeDir) throws IOException, URISyntaxException {
		try ( Repository repository = GitUtil.buildRepository(gitWorkingTreeDir) ) {
			StoredConfig storedConfig = repository.getConfig();
			List<RemoteConfig> remotes = RemoteConfig.getAllRemoteConfigs(storedConfig);
            // TreeMap will sort by remote name
            TreeMap<String, RemoteConfig> sortedRemotes = new TreeMap<>();
            for (RemoteConfig remoteConfig : remotes) {
                sortedRemotes.put(remoteConfig.getName(), remoteConfig);
            }
            // Populate the resulting list ordered by remote name
            return populateRemoteList(sortedRemotes); 
		} // close Repository
	}
	
	private static List<String> populateRemoteList(TreeMap<String, RemoteConfig> sortedRemotes) {
		List<String> list = new LinkedList<>();
        for (Map.Entry<String, RemoteConfig> entry : sortedRemotes.entrySet()) {
            String remoteName = entry.getKey();
            RemoteConfig remoteConfig = entry.getValue();
            String paddedName = String.format("%-8s", remoteName); // pad to 8 chars (left aligned)
            
            // fetch first
            for (URIish uri : remoteConfig.getURIs()) {
            	list.add(paddedName + " : " + uri.toString() + " (fetch)" );
            }

            // push after 
            // To mimic 'git remote -v' exactly, we need to fall back to getURIs() if there are no pushURIs defined
            List<URIish> pushUris = remoteConfig.getPushURIs();
            if (pushUris.isEmpty()) {
            	// not defined (not explicitly configured) => fall back to getURIs() 
                pushUris = remoteConfig.getURIs();
            }
            for (URIish uri : pushUris) {
                list.add(paddedName + " : " + uri.toString() + " (push)");
            }
        }
		return list;
	}
}
