package shifoo.com.app.Models;

public class chooseHeroesModel {

    private String  hero_id="";
    private String  hero_name="";
    private String  hero_profession="";
    private String hero_short_description="";
    private String  cover_photos="";
    private String fans_count="";
    private int setSelected = 0;

    private String sort_order="";

    private String hero_thumbnail="";

    public String getHero_name() {
        return hero_name;
    }

    public int getSetSelected() {
        return setSelected;
    }

    public void setSetSelected(int setSelected) {
        this.setSelected = setSelected;
    }

    public void setHero_name(String hero_name) {
        this.hero_name = hero_name;
    }

    public String getCover_photos() {
        return cover_photos;
    }

    public void setCover_photos(String cover_photos) {
        this.cover_photos = cover_photos;
    }

    public String getHero_id() {
        return hero_id;
    }

    public void setHero_id(String hero_id) {
        this.hero_id = hero_id;
    }

    public String getHero_profession() {
        return hero_profession;
    }

    public void setHero_profession(String hero_profession) {
        this.hero_profession = hero_profession;
    }

    public String getHero_short_description() {
        return hero_short_description;
    }

    public void setHero_short_description(String hero_short_description) {
        this.hero_short_description = hero_short_description;
    }

    public String getFans_count() {
        return fans_count;
    }

    public void setFans_count(String fans_count) {
        this.fans_count = fans_count;
    }

    public String getSort_order() {
        return sort_order;
    }

    public void setSort_order(String sort_order) {
        this.sort_order = sort_order;
    }

    public String getHero_thumbnail() {
        return hero_thumbnail;
    }

    public void setHero_thumbnail(String hero_thumbnail) {
        this.hero_thumbnail = hero_thumbnail;
    }
}
