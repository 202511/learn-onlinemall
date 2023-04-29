<template>
  <el-dialog :visible.sync="visible" :title="!dataForm.id ? $t('add') : $t('update')" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" :label-width="$i18n.locale === 'en-US' ? '120px' : '80px'">
          <el-form-item label="商品id" prop="spuId">
          <el-input v-model="dataForm.spuId" placeholder="商品id"></el-input>
      </el-form-item>
          <el-form-item label="属性id" prop="attrId">
          <el-input v-model="dataForm.attrId" placeholder="属性id"></el-input>
      </el-form-item>
          <el-form-item label="属性名" prop="attrName">
          <el-input v-model="dataForm.attrName" placeholder="属性名"></el-input>
      </el-form-item>
          <el-form-item label="属性值" prop="attrValue">
          <el-input v-model="dataForm.attrValue" placeholder="属性值"></el-input>
      </el-form-item>
          <el-form-item label="顺序" prop="attrSort">
          <el-input v-model="dataForm.attrSort" placeholder="顺序"></el-input>
      </el-form-item>
          <el-form-item label="快速展示【是否展示在介绍上；0-否 1-是】" prop="quickShow">
          <el-input v-model="dataForm.quickShow" placeholder="快速展示【是否展示在介绍上；0-否 1-是】"></el-input>
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
        spuId: '',
        attrId: '',
        attrName: '',
        attrValue: '',
        attrSort: '',
        quickShow: ''
      }
    }
  },
  computed: {
    dataRule () {
      return {
        spuId: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        attrId: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        attrName: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        attrValue: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        attrSort: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        quickShow: [
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
      this.$http.get(`/product/productattrvalue/${this.dataForm.id}`).then(({ data: res }) => {
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
        this.$http[!this.dataForm.id ? 'post' : 'put']('/product/productattrvalue/', this.dataForm).then(({ data: res }) => {
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
