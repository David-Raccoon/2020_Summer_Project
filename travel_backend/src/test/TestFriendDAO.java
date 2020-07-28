package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import main.dao.FriendDAO;
import main.domian.Friend;

public class TestFriendDAO {
	private static FriendDAO friendDAO = new FriendDAO();

	@Test
	public void testFindFriend() {
		ArrayList<Friend> friends = friendDAO.findFriendByUid1(1);
		assertEquals(2, friends.get(0).getUid2());
		assertEquals(5, friends.get(1).getUid2());
		Friend friend = friendDAO.findFriendById(3);
		assertEquals(3, friend.getFriendId());
		assertEquals(1, friend.getUid1());
		assertEquals(5, friend.getUid2());
		assertEquals("inviting", friend.getState());
		friend = friendDAO.findFriendByUid(1, 5);
		assertEquals(3, friend.getFriendId());
		assertEquals(1, friend.getUid1());
		assertEquals(5, friend.getUid2());
		assertEquals("inviting", friend.getState());
		assertNull(friendDAO.findFriendByUid(1, 1));
	}

	@Test
	public void test_Delete_Create_Update_Friend() {
		Friend friend = friendDAO.findFriendById(3);
		assertTrue(friendDAO.deleteFriend(friend.getFriendId()));
		assertEquals(3, friendDAO.findFreeFriendId());
		assertTrue(friendDAO.createFriend(friend));
		assertEquals(3, friend.getFriendId());
		assertEquals(1, friend.getUid1());
		assertEquals(5, friend.getUid2());
		assertEquals("inviting", friend.getState());
		assertTrue(friendDAO.updateFriendState(3, "unlimited"));
		assertEquals("umlimited", friendDAO.findFriendById(3).getState());
		assertTrue(friendDAO.updateFriendState(3, "inviting"));
		assertEquals("inviting", friendDAO.findFriendById(3).getState());
	}

}
