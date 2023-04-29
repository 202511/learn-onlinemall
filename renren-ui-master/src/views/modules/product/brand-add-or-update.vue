<template>
  <el-dialog :visible.sync="visible" :title="!dataForm.brandId ? $t('add') : $t('update')" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" :label-width="$i18n.locale === 'en-US' ? '120px' : '80px'">
          <el-form-item label="品牌名" prop="name">
          <el-input v-model="dataForm.name" placeholder="品牌名"></el-input>
      </el-form-item>
          <el-form-item label="品牌logo地址" prop="logo">
          <single-upload v-model="dataForm.logo"></single-upload>
      </el-form-item>
          <el-form-item label="介绍" prop="descript">
          <el-input v-model="dataForm.descript" placeholder="介绍"></el-input>
      </el-form-item>
          <el-form-item label="显示状态[0-不显示；1-显示]" prop="showStatus">
          <el-input v-model="dataForm.showStatus" placeholder="显示状态[0-不显示；1-显示]"></el-input>
      </el-form-item>
          <el-form-item label="检索首字母" prop="firstLetter">
          <el-input v-model="dataForm.firstLetter" placeholder="检索首字母"></el-input>
      </el-form-item>
          <el-form-item label="排序" prop="sort">
          <el-input v-model="dataForm.sort" placeholder="排序"></el-input>
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
import singleUpload from '../../../components/upload/singleUpload.vue'
export default {
  components: { singleUpload },
  data () {
    return {
      visible: false,
      dataForm: {
        brandId: '',
        name: '',
        logo: '',
        descript: '',
        showStatus: '',
        firstLetter: '',
        sort: ''
      }
    }
  },
  computed: {
    dataRule () {
      return {
        name: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        logo: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        descript: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        showStatus: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        firstLetter: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        sort: [
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
        if (this.dataForm.brandId) {
          this.getInfo()
        }
      })
    },
    // 获取信息
    getInfo () {
      this.$http.get(`/product/brand/${this.dataForm.brandId}`).then(({ data: res }) => {
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
        this.$http[!this.dataForm.brandId ? 'post' : 'put']('/product/brand/', this.dataForm).then(({ data: res }) => {
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
