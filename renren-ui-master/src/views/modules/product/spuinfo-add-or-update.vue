<template>
  <el-dialog :visible.sync="visible" :title="!dataForm.id ? $t('add') : $t('update')" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" :label-width="$i18n.locale === 'en-US' ? '120px' : '80px'">
          <el-form-item label="商品名称" prop="spuName">
          <el-input v-model="dataForm.spuName" placeholder="商品名称"></el-input>
      </el-form-item>
          <el-form-item label="商品描述" prop="spuDescription">
          <el-input v-model="dataForm.spuDescription" placeholder="商品描述"></el-input>
      </el-form-item>
          <el-form-item label="所属分类id" prop="catalogId">
          <el-input v-model="dataForm.catalogId" placeholder="所属分类id"></el-input>
      </el-form-item>
          <el-form-item label="品牌id" prop="brandId">
          <el-input v-model="dataForm.brandId" placeholder="品牌id"></el-input>
      </el-form-item>
          <el-form-item label="" prop="weight">
          <el-input v-model="dataForm.weight" placeholder=""></el-input>
      </el-form-item>
          <el-form-item label="上架状态[0 - 新建，1 - 上架，2 - 下架]" prop="publishStatus">
          <el-input v-model="dataForm.publishStatus" placeholder="上架状态[0 - 新建，1 - 上架，2 - 下架]"></el-input>
      </el-form-item>
          <el-form-item label="" prop="createTime">
          <el-input v-model="dataForm.createTime" placeholder=""></el-input>
      </el-form-item>
          <el-form-item label="" prop="updateTime">
          <el-input v-model="dataForm.updateTime" placeholder=""></el-input>
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
        id: '',
        spuName: '',
        spuDescription: '',
        catalogId: '',
        brandId: '',
        weight: '',
        publishStatus: '',
        createTime: '',
        updateTime: ''
      }
    }
  },
  computed: {
    dataRule () {
      return {
        spuName: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        spuDescription: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        catalogId: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        brandId: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        weight: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        publishStatus: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        updateTime: [
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
        if (this.dataForm.id) {
          this.getInfo()
        }
      })
    },
    // 获取信息
    getInfo () {
      this.$http.get(`/product/spuinfo/${this.dataForm.id}`).then(({ data: res }) => {
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
        this.$http[!this.dataForm.id ? 'post' : 'put']('/product/spuinfo/', this.dataForm).then(({ data: res }) => {
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
