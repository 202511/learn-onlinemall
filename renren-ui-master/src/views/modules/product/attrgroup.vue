<template>
  <el-card shadow="never" class="aui-card--fill">
    <div class="mod-product__attrgroup}">
      <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
        <el-form-item>
          <el-input v-model="dataForm.attrGroupId" placeholder="attrGroupId" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="getDataList()">{{ $t('query') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="info" @click="exportHandle()">{{ $t('export') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button v-if="$hasPermission('product:attrgroup:save')" type="primary" @click="addOrUpdateHandle()">{{ $t('add') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button v-if="$hasPermission('product:attrgroup:delete')" type="danger" @click="deleteHandle()">{{ $t('deleteBatch') }}</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="dataListLoading" :data="dataList" border @selection-change="dataListSelectionChangeHandle" style="width: 100%;">
        <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
        <el-table-column prop="attrGroupId" label="分组id" header-align="center" align="center"></el-table-column>
        <el-table-column prop="attrGroupName" label="组名" header-align="center" align="center"></el-table-column>
        <el-table-column prop="sort" label="排序" header-align="center" align="center"></el-table-column>
        <el-table-column prop="descript" label="描述" header-align="center" align="center"></el-table-column>
        <el-table-column prop="icon" label="组图标" header-align="center" align="center"></el-table-column>
        <el-table-column prop="catelogId" label="所属分类id" header-align="center" align="center"></el-table-column>
        <el-table-column  :label="$t('handle')" fixed="right" header-align="center" align="center" width="150">
          <template slot-scope="scope">
            <el-button v-if="$hasPermission('product:attrgroup:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row.attrGroupId)">{{ $t('update') }}</el-button>
            <el-button v-if="$hasPermission('product:attrgroup:delete')" type="text" size="small" @click="deleteHandle(scope.row.attrGroupId)">{{ $t('delete') }}</el-button>
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
import AddOrUpdate from './attrgroup-add-or-update'
export default {
  mixins: [mixinViewModule],
  data () {
    return {
      mixinViewModuleOptions: {
        getDataListURL: '/product/attrgroup/page',
        getDataListIsPage: true,
        exportURL: '/product/attrgroup/export',
        deleteURL: '/product/attrgroup',
        deleteIsBatch: true
      },
      dataForm: {
        attrGroupId: ''
      }
    }
  },
  components: {
    AddOrUpdate
  }
}
</script>
