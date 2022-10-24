let currentUser = {
    user : {},
    update: async () => {
        currentUser.user = await fetch('/api/user')
            .then(response => response.json())
        await updateHeader();
        await updateCurrentUserInfo()

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
    getRoleLabelList: () => {
        let roles = []
        currentUser.user.roles.forEach(role => roles.push(role.label))
        return roles
    }
}

let users = {
    userMap: new Map(),
    update: async () => {
        let userList = await fetch('/api/admin/users').then(response => response.json())
        userList.forEach(user => users.userMap.set(user.id, user))
    },
    remove: async (id) => {
        users.userMap.delete(id)
    },
    save: async (user) => {
        users.userMap.set(user.id, user)
    }
}

let allRoles = {
    list: {},
    update: async () => {
        allRoles.list = await fetch('/api/admin/roles')
            .then(response => response.json())
    }
}

async function updateHeader() {
    $('#currentEmail').text(currentUser.user.email)
    $('#currentUserRoles').text(currentUser.getRoleLabelList().join(" "));
}

async function updateCurrentUserInfo() {
    let row = $('#userInfoTable tbody tr:first')
    row.find('td').eq(0).text(currentUser.user.id)
    row.find('td').eq(1).text(currentUser.user.name)
    row.find('td').eq(2).text(currentUser.user.lastname)
    row.find('td').eq(3).text(currentUser.user.age)
    row.find('td').eq(4).text(currentUser.user.email)
    row.find('td').eq(5).text(currentUser.getRoleLabelList().join(" "))
}
