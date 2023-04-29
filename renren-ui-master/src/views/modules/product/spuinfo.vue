<template>
  <el-card shadow="never" class="aui-card--fill">
    <div class="mod-product__spuinfo}">
      <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
        <el-form-item>
          <el-input v-model="dataForm.id" placeholder="id" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="getDataList()">{{ $t('query') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="info" @click="exportHandle()">{{ $t('export') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button v-if="$hasPermission('product:spuinfo:save')" type="primary" @click="addOrUpdateHandle()">{{ $t('add') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button v-if="$hasPermission('product:spuinfo:delete')" type="danger" @click="deleteHandle()">{{ $t('deleteBatch') }}</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="dataListLoading" :data="dataList" border @selection-change="dataListSelectionChangeHandle" style="width: 100%;">
        <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
        <el-table-column prop="id" label="商品id" header-align="center" align="center"></el-table-column>
        <el-table-column prop="spuName" label="商品名称" header-align="center" align="center"></el-table-column>
        <el-table-column prop="spuDescription" label="商品描述" header-align="center" align="center"></el-table-column>
        <el-table-column prop="catalogId" label="所属分类id" header-align="center" align="center"></el-table-column>
        <el-table-column prop="brandId" label="品牌id" header-align="center" align="center"></el-table-column>
        <el-table-column prop="weight" label="" header-align="center" align="center"></el-table-column>
        <el-table-column prop="publishStatus" label="上架状态[0 - 新建，1 - 上架，2 - 下架]" header-align="center" align="center"></el-table-column>
        <el-table-column prop="createTime" label="" header-align="center" align="center"></el-table-column>
        <el-table-column prop="updateTime" label="" header-align="center" align="center"></el-table-column>
        <el-table-column :label="$t('handle')" fixed="right" header-align="center" align="center" width="150">
          <template slot-scope="scope">
            <el-button v-if="$hasPermission('product:spuinfo:update')" type="text" size="small" @click="spuUp(scope.row.id)">上架</el-button>
            <el-button v-if="$hasPermission('product:spuinfo:delete')" type="text" size="small" @click="deleteHandle(scope.row.id)">{{ $t('delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        :current-page="page"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="limit"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="pageSizeChangeHandle"
        @current-change="pageCurrentChangeHandle">
      </el-pagination>
      <!-- 弹窗, 新增 / 修改 -->
      <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    </div>
  </el-card>
</template>

<script>
import mixinViewModule from '@/mixins/view-module'
import AddOrUpdate from './spuinfo-add-or-update'
export default {
  mixins: [mixinViewModule],
  data () {
    return {
      mixinViewModuleOptions: {
        getDataListURL: '/product/spuinfo/page',
        getDataListIsPage: true,
        exportURL: '/product/spuinfo/export',
        deleteURL: '/product/spuinfo',
        deleteIsBatch: true
      },
      dataForm: {
        id: ''
      }
    }
  },
  components: {
    AddOrUpdate
  },
  methods:{
    spuUp(id)
    {
      console.log(id)
      this.$http.get(`/product/spuinfo/up/${id}`).then(({ data: res }) => {
        console.log(res)
      }).catch(() => {})
    }
  }
}
</script>
