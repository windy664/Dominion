package cn.lunadeer.dominion.configuration;

import cn.lunadeer.dominion.uis.AllDominion;
import cn.lunadeer.dominion.uis.MainMenu;
import cn.lunadeer.dominion.uis.TitleList;
import cn.lunadeer.dominion.uis.dominion.DominionList;
import cn.lunadeer.dominion.uis.dominion.DominionManage;
import cn.lunadeer.dominion.uis.dominion.manage.EnvSetting;
import cn.lunadeer.dominion.uis.dominion.manage.GuestSetting;
import cn.lunadeer.dominion.uis.dominion.manage.group.GroupList;
import cn.lunadeer.dominion.uis.dominion.manage.group.GroupManage;
import cn.lunadeer.dominion.uis.dominion.manage.group.GroupSetting;
import cn.lunadeer.dominion.uis.dominion.manage.member.MemberList;
import cn.lunadeer.dominion.uis.dominion.manage.member.MemberSetting;
import cn.lunadeer.dominion.utils.configuration.ConfigurationFile;
import cn.lunadeer.dominion.utils.configuration.Heads;

@Heads({
        "Brief Description:",
        "    This file defines the user interface for the chest GUI in Dominion.",
        "    Both name and lore support PlaceholderAPI and color codes.",
        "",
        "For list view layout:",
        "    You can change the number or position of the item symbol to control how",
        "    many and where items are displayed.",
        "",
        "For button material:",
        "    Use the material name in uppercase, e.g., STONE, DIAMOND_SWORD.",
        "    If you want to use player heads with textures, we support three formats:",
        "        1. PLAYER_HEAD;B64;{base64 texture value}",
        "        2. PLAYER_HEAD;URL;{texture image URL}",
        "        3. PLAYER_HEAD;NAME;{player name}",
        "    The first format uses a base64-encoded texture value, while the second",
        "    uses a URL to an image. The URL must point to a Mojang server skin image.",
        "    You can search textures on e.g. https://mcheads.ru/ to get the base64 ",
        "    texture value or the URL you need.",
})
public class ChestUserInterface extends ConfigurationFile {
    public static MainMenu.MainMenuCui mainMenuCui = new MainMenu.MainMenuCui();
    public static AllDominion.AllDominionCui allDominionCui = new AllDominion.AllDominionCui();
    public static TitleList.TitleListCui titleListCui = new TitleList.TitleListCui();
    public static DominionList.DominionListCui dominionListCui = new DominionList.DominionListCui();
    public static DominionManage.DominionManageCui dominionManageCui = new DominionManage.DominionManageCui();
    public static EnvSetting.EnvSettingCui envSettingCui = new EnvSetting.EnvSettingCui();
    public static GuestSetting.GuestSettingCui guestSettingCui = new GuestSetting.GuestSettingCui();
    public static GroupList.GroupListCui groupListCui = new GroupList.GroupListCui();
    public static GroupManage.GroupManageCUI groupManageCUI = new GroupManage.GroupManageCUI();
    public static GroupSetting.GroupSettingCui groupSettingCui = new GroupSetting.GroupSettingCui();
    public static MemberList.MemberListCui memberListCui = new MemberList.MemberListCui();
    public static MemberSetting.MemberSettingCui memberSettingCui = new MemberSetting.MemberSettingCui();
}
