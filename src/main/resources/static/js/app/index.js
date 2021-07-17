var main = {
    init : function () {
        var _this = this;
        $('#btn-save').on('click', function () {
            _this.save();
        });
        $('#btn-search').on('click', function () {
            _this.search();
        });

    },
    save : function () {
        var data = {
            name: $('#name').val(),
            email: $('#email').val(),
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/user',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data),
            success: function () {
                localStorage.setItem('subscribe','1');
            }
        }).done(function() {
            alert('유저가 등록되었습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    search : function () {
        var date = $('#date').val();

        $.ajax({
            type: 'GET',
        }).done(function() {
            window.location.href = '/news/'+date;
        }).fail(function (error) {
            alert('해당 날짜의 뉴스는 존재하지 않습니다.');
            alert(JSON.stringify(error));
        });
    }
};
main.init();