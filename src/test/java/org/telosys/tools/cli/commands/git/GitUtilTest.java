package org.telosys.tools.cli.commands.git;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class GitUtilTest {

	@Test
	public void testIsGitUrl() {
		assertTrue( GitUtil.isGitUrl("https://github.com/user/repo.git") );
		assertTrue( GitUtil.isGitUrl("https://github.com/user/repo") );
		assertTrue( GitUtil.isGitUrl("http://github.com/user/repo.git") );
		assertTrue( GitUtil.isGitUrl("http://github.com/user/repo") );
		assertTrue( GitUtil.isGitUrl("ssh://git@github.com/user/repo.git") );
		assertTrue( GitUtil.isGitUrl("ssh://git@github.com/user/repo") );
		assertTrue( GitUtil.isGitUrl("git://github.com/user/repo.git") );
		assertTrue( GitUtil.isGitUrl("git://github.com/user/repo") );
		assertTrue( GitUtil.isGitUrl("file:///home/user/repo.git") );
		assertTrue( GitUtil.isGitUrl("file:///home/user/repo") );
		assertTrue( GitUtil.isGitUrl("git@github.com:user/repo.git") );
		assertTrue( GitUtil.isGitUrl("git@github.com:user/repo") );

		assertTrue( GitUtil.isGitUrl("/aa/bb/repo.git") );
		assertTrue( GitUtil.isGitUrl("/aa/bb/repo") );
		assertTrue( GitUtil.isGitUrl("./aa/bb/repo.git") );
		assertTrue( GitUtil.isGitUrl("C:/Users/me/projects/myrepo.git") );
		assertTrue( GitUtil.isGitUrl("C:\\aaa\\bbb\\myrepo.git") );
		assertTrue( GitUtil.isGitUrl("//SERVER/xx/repo.git") );
		
		// Usable as model/bundle name 
		assertFalse( GitUtil.isGitUrl("abcd") );
		assertFalse( GitUtil.isGitUrl("ab-cd01234") );
		assertFalse( GitUtil.isGitUrl("@xyz") );
		assertFalse( GitUtil.isGitUrl("abcd@xyz") );
	}

}
