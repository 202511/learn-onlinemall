<template>
  <el-dialog :visible.sync="visible" :title="!dataForm.attrGroupId ? $t('add') : $t('update')" :close-on-click-modal="false" :close-on-press-escape="false">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmitHandle()" :label-width="$i18n.locale === 'en-US' ? '120px' : '80px'">
          <el-form-item label="组名" prop="attrGroupName">
          <el-input v-model="dataForm.attrGroupName" placeholder="组名"></el-input>
      </el-form-item>
          <el-form-item label="排序" prop="sort">
          <el-input v-model="dataForm.sort" placeholder="排序"></el-input>
      </el-form-item>
          <el-form-item label="描述" prop="descript">
          <el-input v-model="dataForm.descript" placeholder="描述"></el-input>
      </el-form-item>
          <el-form-item label="组图标" prop="icon">
          <el-input v-model="dataForm.icon" placeholder="组图标"></el-input>
      </el-form-item>
          <el-form-item label="所属分类id" prop="catelogId">
          <el-input v-model="dataForm.catelogId" placeholder="所属分类id"></el-input>
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
        attrGroupId: '',
        attrGroupName: '',
        sort: '',
        descript: '',
        icon: '',
        catelogId: ''
      }
    }
  },
  computed: {
    dataRule () {
      return {
        attrGroupName: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        sort: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        descript: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        icon: [
          { required: true, message: this.$t('validate.required'), trigger: 'blur' }
        ],
        catelogId: [
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
        if (this.dataForm.attrGroupId) {
          this.getInfo()
        }
      })
    },
    // 获取信息
    getInfo () {
      this.$http.get(`/product/attrgroup/${this.dataForm.attrGroupId}`).then(({ data: res }) => {
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
        this.$http[!this.dataForm.attrGroupId ? 'post' : 'put']('/product/attrgroup/', this.dataForm).then(({ data: res }) => {
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
