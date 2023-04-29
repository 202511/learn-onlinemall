<template>
  <el-dialog :visible.sync="visible" :title="!dataForm.id ? $t('add') : $t('update')" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" :label-width="$i18n.locale === 'en-US' ? '120px' : '80px'">
          <el-form-item label="" prop="branchId">
          <el-input v-model="dataForm.branchId" placeholder=""></el-input>
      </el-form-item>
          <el-form-item label="" prop="xid">
          <el-input v-model="dataForm.xid" placeholder=""></el-input>
      </el-form-item>
          <el-form-item label="" prop="context">
          <el-input v-model="dataForm.context" placeholder=""></el-input>
      </el-form-item>
          <el-form-item label="" prop="rollbackInfo">
          <el-input v-model="dataForm.rollbackInfo" placeholder=""></el-input>
      </el-form-item>
          <el-form-item label="" prop="logStatus">
          <el-input v-model="dataForm.logStatus" placeholder=""></el-input>
      </el-form-item>
          <el-form-item label="" prop="logCreated">
          <el-input v-model="dataForm.logCreated" placeholder=""></el-input>
      </el-form-item>
          <el-form-item label="" prop="logModified">
          <el-input v-model="dataForm.logModified" placeholder=""></el-input>
      </el-form-item>
          <el-form-item label="" prop="ext">
          <el-input v-model="dataForm.ext" placeholder=""></el-input>
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
        branchId: '',
        xid: '',
        context: '',
        rollbackInfo: '',
        logStatus: '',
        logCreated: '',
        logModified: '',
        ext: ''
      }
    }
  },
  computed: {
    dataRule () {
      return {
        branchId: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        xid: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        context: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        rollbackInfo: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        logStatus: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        logCreated: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        logModified: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        ext: [
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
      this.$http.get(`/product/undolog/${this.dataForm.id}`).then(({ data: res }) => {
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
        this.$http[!this.dataForm.id ? 'post' : 'put']('/product/undolog/', this.dataForm).then(({ data: res }) => {
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
