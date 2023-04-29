<template>
  <el-dialog :visible.sync="visible" :title="!dataForm.attrId ? $t('add') : $t('update')" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" :label-width="$i18n.locale === 'en-US' ? '120px' : '80px'">
          <el-form-item label="属性名" prop="attrName">
          <el-input v-model="dataForm.attrName" placeholder="属性名"></el-input>
      </el-form-item>
          <el-form-item label="是否需要检索[0-不需要，1-需要]" prop="searchType">
          <el-input v-model="dataForm.searchType" placeholder="是否需要检索[0-不需要，1-需要]"></el-input>
      </el-form-item>
          <el-form-item label="值类型[0-为单个值，1-可以选择多个值]" prop="valueType">
          <el-input v-model="dataForm.valueType" placeholder="值类型[0-为单个值，1-可以选择多个值]"></el-input>
      </el-form-item>
          <el-form-item label="属性图标" prop="icon">
          <el-input v-model="dataForm.icon" placeholder="属性图标"></el-input>
      </el-form-item>
          <el-form-item label="可选值列表[用逗号分隔]" prop="valueSelect">
          <el-input v-model="dataForm.valueSelect" placeholder="可选值列表[用逗号分隔]"></el-input>
      </el-form-item>
          <el-form-item label="属性类型[0-销售属性，1-基本属性" prop="attrType">
          <el-input v-model="dataForm.attrType" placeholder="属性类型[0-销售属性，1-基本属性"></el-input>
      </el-form-item>
          <el-form-item label="启用状态[0 - 禁用，1 - 启用]" prop="enable">
          <el-input v-model="dataForm.enable" placeholder="启用状态[0 - 禁用，1 - 启用]"></el-input>
      </el-form-item>
          <el-form-item label="所属分类" prop="catelogId">
          <el-input v-model="dataForm.catelogId" placeholder="所属分类"></el-input>
      </el-form-item>
          <el-form-item label="快速展示【是否展示在介绍上；0-否 1-是】，在sku中仍然可以调整" prop="showDesc">
          <el-input v-model="dataForm.showDesc" placeholder="快速展示【是否展示在介绍上；0-否 1-是】，在sku中仍然可以调整"></el-input>
      </el-form-item>
      </el-form>
    <template slot="footer">
      <el-button @click="visible = false">{{ $t('cancel') }}</el-button>
      <el-button type="primary" @click="dataFormSubmitHandle()">{{ $t('confirm') }}</el-button>
    </template>
  </el-dialog>
</template>

<script>
import debounce from 'lodash/debounce'
export default {
  data () {
    return {
      visible: false,
      dataForm: {
        attrId: '',
        attrName: '',
        searchType: '',
        valueType: '',
        icon: '',
        valueSelect: '',
        attrType: '',
        enable: '',
        catelogId: '',
        showDesc: ''
      }
    }
  },
  computed: {
    dataRule () {
      return {
        attrName: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        searchType: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        valueType: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        icon: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        valueSelect: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        attrType: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        enable: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        catelogId: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        showDesc: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    init () {
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.attrId) {
          this.getInfo()
        }
      })
    },
    // 获取信息
    getInfo () {
      this.$http.get(`/product/attr/${this.dataForm.attrId}`).then(({ data: res }) => {
        if (res.code !== 0) {
          return this.$message.error(res.msg)
        }
        this.dataForm = {
          ...this.dataForm,
          ...res.data
        }
      }).catch(() => {})
    },
    // 表单提交
    dataFormSubmitHandle: debounce(function () {
      this.$refs['dataForm'].validate((valid) => {
        if (!valid) {
          return false
        }
        this.$http[!this.dataForm.attrId ? 'post' : 'put']('/product/attr/', this.dataForm).then(({ data: res }) => {
          if (res.code !== 0) {
            return this.$message.error(res.msg)
          }
          this.$message({
            message: this.$t('prompt.success'),
            type: 'success',
            duration: 500,
            onClose: () => {
              this.visible = false
              this.$emit('refreshDataList')
            }
          })
        }).catch(() => {})
      })
    }, 1000, { 'leading': true, 'trailing': false })
  }
}
</script>
