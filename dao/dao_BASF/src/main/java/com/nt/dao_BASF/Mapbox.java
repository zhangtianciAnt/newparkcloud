package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "mapbox")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mapbox extends BaseModel {

    /**
     * 数据主键ID
     */
    private String _id;
    /**
     * 地图层级树id
     */
    private String mapid;
    /**
     * 地图层级树name
     */
    private String name;
    /**
     * 父级地图id
     */
    private String parentid;
    /**
     * 简称
     */
    private String shortname;

    /**
     * 地图等级
     */
    private String level;

    /**
     * 位置路径
     */
    private String cascname;

    /**
     * 位置路径id
     */
    private String cascids;

    /**
     * 监测值
     */
    private List<Children> children;

    @Data
    //二层
    public static class Children {

        /**
         * 地图层级树id
         */
        private String mapid;
        /**
         * 地图层级树name
         */
        private String name;
        /**
         * 父级地图id
         */
        private String parentid;
        /**
         * 简称
         */
        private String shortname;

        /**
         * 地图等级
         */
        private String level;

        /**
         * 位置路径
         */
        private String cascname;

        /**
         * 位置路径id
         */
        private String cascids;

        /**
         * 监测值
         */
        private List<Children1> children1;
    }

    @Data
    //三层
    public static class Children1 {

        /**
         * 地图层级树id
         */
        private String mapid;
        /**
         * 地图层级树name
         */
        private String name;
        /**
         * 父级地图id
         */
        private String parentid;
        /**
         * 简称
         */
        private String shortname;

        /**
         * 地图等级
         */
        private String level;

        /**
         * 位置路径
         */
        private String cascname;

        /**
         * 位置路径id
         */
        private String cascids;

        /**
         * 监测值
         */
        private List<Children2> children2;
    }

    @Data
    //四层
    public static class Children2 {

        /**
         * 地图层级树id
         */
        private String mapid;
        /**
         * 地图层级树name
         */
        private String name;
        /**
         * 父级地图id
         */
        private String parentid;
        /**
         * 简称
         */
        private String shortname;

        /**
         * 地图等级
         */
        private String level;

        /**
         * 位置路径
         */
        private String cascname;

        /**
         * 位置路径id
         */
        private String cascids;
    }
}