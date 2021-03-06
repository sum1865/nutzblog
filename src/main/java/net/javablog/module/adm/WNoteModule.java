package net.javablog.module.adm;

import net.javablog.bean.tb_book;
import net.javablog.bean.tb_seris;
import net.javablog.service.NoteService;
import net.javablog.service.SerisService;
import net.javablog.util.CurrentUserUtils;
import net.javablog.util.Translates;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.filter.CheckSession;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@IocBean
@Filters(@By(type = CheckSession.class, args = {CurrentUserUtils.CUR_USER, "/adm/login"}))
public class WNoteModule {

    @Inject
    private NoteService noteService;

    @Inject
    private SerisService serisService;


    @At("/adm/wnote")
    @Ok("fm:adm.note.wnote")
    public NutMap wnote(@Param(value = "_id", df = "0") int id) {
        NutMap out = new NutMap();
        out.put("sidebar_openposition", "#li3");
        out.put("sidebar_activeposition", "#li3li2");
        tb_book tb = new tb_book();
        if (id > 0) {
            tb = noteService.fetch(id);
        }
        out.put("item", tb);
        return out;
    }

    @At("/adm/books_mgr/doaddup")
    @Ok("json")
    public String doaddup(@Param("..") tb_book tb) {
        tb.setUt(new Date());
        tb.set_booktitleen(Translates.trans(tb.get_booktitle()));
        if (tb.get_id() > 0) {
            noteService.update(tb);
        } else {
            tb.setCt(new Date());
            noteService.insert(tb);
        }
        return tb.get_id() + "";
    }


    @At("/adm/books_mgr/doaddup_inlist")
    @Ok("json")
    public Map doaddup_inlist(@Param("..") tb_seris tbin) {

        HashMap map = new HashMap();

        if (tbin.get_id() > 0) {
            tb_seris temp = serisService.fetch(tbin.get_id());
            tbin.set_index_inbook(temp.get_index_inbook());
            serisService.update(tbin);
        } else {

            tb_seris count = serisService.fetch(Cnd.where("_bookid", "=", tbin.get_bookid()).and("_id", "!=", tbin.get_id()).orderBy("_index_inbook", "desc"));
            if (count == null) {
                tbin.set_index_inbook(1);
            } else {
                tbin.set_index_inbook(count.get_index_inbook() + 1);
            }
            serisService.insert(tbin);
        }

        map.put("status", "ok");
        map.put("id", tbin.get_id());
        return map;
    }


    @At("/adm/books_mgr/del")
    @Ok("json")
    public String delbook(@Param("_id") int id) {
        int count = serisService.count(Cnd.where("_bookid", "=", id));
        if (count > 0) {
            return "读书笔记下还有章节没删除，不能删除当前笔记。";
        }
        noteService.delete(id);
        return "ok";
    }


}
