let currentUser = {
    user : {},
    update: async () => {
        currentUser.user = await fetch('/api/user')
    },
    hasRole: (name) => {
        let result = false
        currentUser.user.roles.forEach(role => {
            if (role.name === name) {
                result = true
            }
        })
        return result
    },

}

let allRoles = {
    list: {},
    update: async () => {
        allRoles.list = await fetch('/api/admin/roles')
            .then(response => response.json())
        await updateRolesInNewUserForm()
    }
}
